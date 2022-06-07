package pii.service;

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
import pii.repository.ExpenseRepository;

@Service
public class ExpenseService {
	
	@Autowired
	private ExpenseRepository repository;
	
	@Autowired
	private ExpenseDTOMapper dtoMapper;
	
	@Autowired
	private CardService cardService;
	
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
		
		return repository.save(expense)
				.map(dtoMapper::objToDTO);
	}
	
	public Optional<ExpenseDTO> update(Long id, ExpenseDTO dto) {
		var expense = dtoMapper.dtoToObj(dto);
		
		validateExpense(expense);
		
		return repository.update(id, expense)
				.map(dtoMapper::objToDTO);
	}
	
	public Boolean delete(Long id) {
		return repository.delete(id);
	}
	
	private void validateExpense(Expense expense) {
		var card = cardService.findByNumber(expense.cardId())
				.orElseThrow(() -> new NotFoundException("Cartão não encontrado."));
		
		if ((expense.paymentMethod() == PaymentMethod.CREDIT && CardType.valueOf(card.type()).get() != CardType.CREDIT)
				|| (expense.paymentMethod() == PaymentMethod.DEBIT && CardType.valueOf(card.type()).get() != CardType.DEBIT)) {
			throw new ConflictException("Cartão imcompatível com método de pagamento utilizado.");
		}
		
		if (expense.paymentMethod() == PaymentMethod.CREDIT && expense.value().compareTo(card.limit()) == 1) {
			throw new ConflictException("Valor da despesa ultrapassa limite do cartão de crédito.");
		} else if (expense.paymentMethod() == PaymentMethod.DEBIT
				&& expense.value().compareTo(card.currentValue()) == 1) {
			throw new ConflictException("Cartão de débito não possui saldo suficiente.");
		}
	}
}
