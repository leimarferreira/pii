package pii.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.ExpenseDTO;
import pii.dto.ExpenseDTOMapper;
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
				addExpenseToInvoices(savedExpense);
			}
		}
		
		return result.map(dtoMapper::objToDTO);
	}
	
	public Optional<ExpenseDTO> update(Long id, ExpenseDTO dto) {
		var expense = dtoMapper.dtoToObj(dto);
		
		validateExpense(expense);
		
		var result = repository.update(id, expense);
		
		if (result.isPresent()) {
			var updatedExpense = result.get();
			
			parcelService.deleteAllByExpenseId(updatedExpense.id());
			
			if (updatedExpense.paymentMethod() == PaymentMethod.CREDIT) {
				addExpenseToInvoices(updatedExpense);
			}
		}
		
		return result.map(dtoMapper::objToDTO);
	}
	
	public Boolean delete(Long id) {
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
		}
		
		// TODO: verificar se a despesa não ultrapassa o limite do cartão de crédito
		// TODO: verificar se a despesa não ultrapassa o saldo do cartão de débito
	}
	
	private void addExpenseToInvoices(Expense expense) {
		var card = cardService.findById(expense.cardId())
				.orElseThrow(() -> new NotFoundException("Cartão não encontrado no sistema."));
		var dueDay = expense.dueDate().getDayOfMonth();
		var currentYear = expense.dueDate().getYear();
		
		var month = LocalDate.ofInstant(Instant.now(), ZoneOffset.UTC).getMonthValue();
		
		if (dueDay > card.dueDate()) {
			++month;
		}
		
		var valuePerParcel = expense.value().divide(new BigDecimal(expense.numberOfParcels()), 2, RoundingMode.HALF_UP);
		
		for (int i = 1; i <= expense.numberOfParcels(); i++, month++) {
			var date = String.format("%d/%d", month, currentYear);
			
			var invoice = invoiceService.getInvoiceByMonthAndCardId(date, expense.cardId())
					.orElseThrow(() -> new ConflictException("Erro ao adicionar despesa à fatura."));
			
			var dueDate = LocalDate.of(currentYear, month, dueDay);
			
			var parcel = new Parcel(
					0, expense.id(), invoice.id(), i, dueDate, valuePerParcel);
			
			parcelService.save(parcel);
			
			if (month == 12) {
				currentYear++;
				month = 1;
			}
		}
	}
}
