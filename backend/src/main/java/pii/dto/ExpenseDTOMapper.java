package pii.dto;

import org.springframework.stereotype.Component;

import pii.model.Expense;

@Component
public class ExpenseDTOMapper {
	
	public ExpenseDTO objToDTO(Expense expense) {
		return new ExpenseDTO(
				expense.id(),
				expense.userId(),
				expense.value(),
				expense.description(),
				expense.categoryId(),
				expense.paymentMethod().getValue(),
				expense.numberOfParcels(),
				expense.isPaid(),
				expense.cardId());
	}
	
	public Expense dtoToObj(ExpenseDTO dto) {
		return new Expense(
				dto.id(),
				dto.userId(),
				dto.value(),
				dto.description(),
				dto.categoryId(),
				dto.paymentMethod(),
				dto.numberOfParcels(),
				dto.isPaid(),
				dto.cardId());
	}
}
