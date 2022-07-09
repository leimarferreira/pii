package pii.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.CardDTO;
import pii.dto.ExpenseDTO;
import pii.dto.ExpenseDTOMapper;
import pii.dto.InvoiceDTO;
import pii.enums.CardType;
import pii.enums.PaymentMethod;
import pii.exception.ConflictException;
import pii.exception.NotFoundException;
import pii.model.Expense;
import pii.model.Parcel;
import pii.repository.ExpenseRepository;

@Service
public class ExpenseService {
	
	@Autowired
	private ExpenseRepository repository;
	
	@Autowired
	private ExpenseDTOMapper dtoMapper;
	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private ParcelService parcelService;
	
	@Autowired
	private UserFinancialDataService userFinancialDataService;
	
	public List<ExpenseDTO> findAll() {
		return repository
				.findAll()
				.stream()
				.map(dtoMapper::objToDTO)
				.toList();
	}
	
	public List<ExpenseDTO> findAllByUserId(Long userId) {
		return repository
				.findAllByUserId(userId)
				.stream()
				.map(dtoMapper::objToDTO)
				.toList();
	}
	
	public List<ExpenseDTO> findAllByCategoryId(Long categoryId) {
		return repository
				.findAllByCategoryId(categoryId)
				.stream()
				.map(dtoMapper::objToDTO)
				.toList();
	}
	
	public Optional<ExpenseDTO> findById(Long id) {
		return repository
				.findById(id)
				.map(dtoMapper::objToDTO);
	}
	
	public Optional<ExpenseDTO> save(ExpenseDTO dto) {
		var expense = dtoMapper.dtoToObj(dto);
		
		validateExpense(expense);
		
		var result = repository.save(expense);
		
		if (result.isPresent()) {
			var savedExpense = result.get();
			
			if (savedExpense.paymentMethod() == PaymentMethod.CREDIT) {
				// TODO: avaliar se é realmente necessário verificar se a despesa está paga.
				if (expense.isPaid()) {
					var card = cardService.findById(expense.cardId()).get();
					
					var cardValue = card.currentValue().add(expense.value());
					var updatedCard = new CardDTO(card.id(), card.userId(), card.number(), card.type(), card.brand(),
							card.limit(), cardValue, card.dueDate());
					cardService.update(card.id(), updatedCard);
				}
				
				try {					
					addExpenseToInvoices(savedExpense);
				} catch (Exception exception) {
					cleanExpense(savedExpense);
					throw exception;
				}
			}
		}
		
		return result.map(dtoMapper::objToDTO);
	}
	
	public Optional<ExpenseDTO> update(Long id, ExpenseDTO dto) {
		var expense = dtoMapper.dtoToObj(dto);
		
		validateExpense(expense);
		
		var existingExpense = repository.findById(id)
				.orElseThrow(() -> new NotFoundException("Despesa não encontrada no sistema."));
		
		if (existingExpense.paymentMethod() == PaymentMethod.CREDIT) {
			cleanExpense(existingExpense);
		}
		
		var result = repository.update(id, expense);
		
		if (result.isPresent()) {
			var updatedExpense = result.get();
			
			try {				
				if (updatedExpense.paymentMethod() == PaymentMethod.CREDIT) {
					// TODO: avaliar se é realmente necessário verificar se a despesa está paga.
					if (updatedExpense.isPaid()) {
						var card = cardService.findById(updatedExpense.cardId()).get();
						
						var cardValue = card.currentValue().add(updatedExpense.value());
						var updatedCard = new CardDTO(card.id(), card.userId(), card.number(), card.type(), card.brand(),
								card.limit(), cardValue, card.dueDate());
						cardService.update(card.id(), updatedCard);
					}
					
					addExpenseToInvoices(updatedExpense);
				}
			} catch (Exception exception) {
				cleanExpense(updatedExpense);
				result = repository.update(id, existingExpense);
				addExpenseToInvoices(existingExpense);
			}
		}
		
		return result.map(dtoMapper::objToDTO);
	}
	
	public Boolean delete(Long id) {
		var existingExpense = repository.findById(id)
				.orElseThrow(() -> new NotFoundException("Despesa não encontrada no sistema."));
		cleanExpense(existingExpense);
		return repository.delete(id);
	}
	
	private void validateExpense(Expense expense) {
		if (expense.paymentMethod() != PaymentMethod.MONEY) {
			var card = cardService.findById(expense.cardId())
					.orElseThrow(() -> new NotFoundException("Cartão não encontrado."));
			
			if ((expense.paymentMethod() == PaymentMethod.CREDIT && CardType.valueOf(card.type()).get() != CardType.CREDIT)
					|| (expense.paymentMethod() == PaymentMethod.DEBIT && CardType.valueOf(card.type()).get() != CardType.DEBIT)) {
				throw new ConflictException("Cartão imcompatível com método de pagamento utilizado.");
			}
			
			if (expense.paymentMethod() == PaymentMethod.CREDIT) {
				if (expense.isPaid() && card.currentValue().add(expense.value()).compareTo(card.limit()) > 0) {
					throw new ConflictException("Despesa ultrapassa o limite do cartão.");
				}
			} else if (expense.paymentMethod() == PaymentMethod.DEBIT) {
				var financialData = userFinancialDataService.getUserFinancialData(expense.userId())
						.orElseThrow(() -> new ConflictException("Erro ao obter receita do usuário."));
				if (expense.value().compareTo(financialData.balance()) > 0) {
					throw new ConflictException("Despesa ultrapassa saldo do usuário.");
				}
			}
		}
	}
	
	private void addExpenseToInvoices(Expense expense) {		
		var card = cardService.findById(expense.cardId())
				.orElseThrow(() -> new NotFoundException("Cartão não encontrado no sistema."));
		var date = expense.dueDate();
		
		if (date.getDayOfMonth() > card.dueDate()) {
			date.plusMonths(1);
		}
		
		var valuePerParcel = expense.value().divide(new BigDecimal(expense.numberOfParcels()), 2, RoundingMode.HALF_UP);
		
		for (int i = 1; i <= expense.numberOfParcels(); i++) {
			var dateStr = String.format("%02d/%d", date.getMonth().getValue(), date.getYear());
			
			var invoice = invoiceService.getInvoiceByMonthAndCardId(dateStr, expense.cardId())
					.orElseThrow(() -> new ConflictException("Erro ao adicionar despesa à fatura."));
			
			
			var parcel = new Parcel(0, expense.id(), invoice.id(), i, date, valuePerParcel);
			
			var updatedInvoice = new InvoiceDTO(invoice.id(), invoice.month(), invoice.cardId(), invoice.value().add(parcel.value()));
			invoiceService.update(invoice.id(), updatedInvoice);
			
			parcelService.save(parcel);
			
			date = date.plusMonths(1);
		}
	}
	
	private void cleanExpense(Expense expense) {
		// TODO: avaliar se é realmente necessário verificar se a despesa está paga.
		if (expense.isPaid()) {			
			var card = cardService.findById(expense.cardId()).get();
			var cardValue = card.currentValue().subtract(expense.value());
			var updatedCard = new CardDTO(card.id(), card.userId(), card.number(), card.type(), card.brand(),
					card.limit(), cardValue, card.dueDate());
			cardService.update(card.id(), updatedCard);
		}
		
		var parcels = parcelService.findAllByExpenseId(expense.id());
		parcels.forEach(parcel -> {
			var invoice = invoiceService.findById(parcel.invoiceId()).get();
			var invoiceValue = invoice.value().subtract(parcel.value());
			var updatedInvoice = new InvoiceDTO(invoice.id(), invoice.month(), invoice.cardId(), invoiceValue);
			invoiceService.update(invoice.id(), updatedInvoice);
		});
		
		parcelService.deleteAllByExpenseId(expense.id());
		invoiceService.deleteAllEmptyInvoices();
	}
}
