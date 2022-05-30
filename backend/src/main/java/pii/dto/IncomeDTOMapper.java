package pii.dto;

import org.springframework.stereotype.Component;

import pii.model.Income;

@Component
public class IncomeDTOMapper {
	
	public IncomeDTO toDTO(Income income) {
		return new IncomeDTO(
				income.id(),
				income.userId(),
				income.value(),
				income.dateAsLong(),
				income.description(),
				income.categoryId());
	}
	
	public Income toObj(IncomeDTO dto) {
		return new Income(
				dto.id(),
				dto.userId(),
				dto.value(),
				dto.date(),
				dto.description(),
				dto.categoryId());
	}
}
