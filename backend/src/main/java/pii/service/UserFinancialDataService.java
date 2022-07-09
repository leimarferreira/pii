package pii.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.UserFinancialData;
import pii.enums.CardType;
import pii.repository.ExpenseRepository;

@Service
public class UserFinancialDataService {

	@Autowired
	private IncomeService incomeService;
	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private CardService cardService;

	public Optional<UserFinancialData> getUserFinancialData(long userId) {
		try {
			var totalIncomes = getTotalIncomes(userId);
			var totalExpenses = getTotalExpenses(userId);
			var totalCards = getTotalCards(userId);
			
			var balance = new BigDecimal(0);
			balance = balance.add(totalCards);
			balance = balance.add(totalIncomes);
			balance = balance.subtract(totalExpenses);
			
			var data = new UserFinancialData(userId, totalExpenses, totalIncomes, totalCards, balance);
			return Optional.of(data);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	private BigDecimal getTotalIncomes(long userId) {
		BigDecimal total = new BigDecimal(0);
		var incomes = incomeService.findAllByUserId(userId);
		for (var income : incomes) {
			total = total.add(income.value());
		}

		return total;
	}

	private BigDecimal getTotalExpenses(long userId) {
		BigDecimal total = new BigDecimal(0);
		var expenses = expenseRepository.findAllByUserId(userId);
		
		for (var expense : expenses) {
			total = total.add(expense.value());
		}
		
		return total;
	}
	
	private BigDecimal getTotalCards(long userId) {
		BigDecimal total = new BigDecimal(0);
		var cards = cardService.findAllByUserId(userId);
		for (var card : cards ) {
			if (card.type() == CardType.CREDIT.getValue()) {
				total = total.add(card.limit());
			} else if (card.type() == CardType.DEBIT.getValue()) {
				total = total.add(card.currentValue());
			}
		}
		
		return total;
	}
}
