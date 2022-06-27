package pii.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.UserFinancialData;
import pii.enums.CardType;

@Service
public class UserFinancialDataService {

	@Autowired
	private IncomeService incomeService;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private CardService cardService;

	public Optional<UserFinancialData> getUserFinancialData(long userId) {
		try {
			var totalIncomes = getTotalIncomes(userId);
			var totalExpenses = getTotalExpenses(userId);
			var totalCards = getTotalCards(userId);
			
			var balance = new BigDecimal(0);
			balance.add(totalCards);
			balance.add(totalIncomes);
			balance.subtract(totalExpenses);
			
			var data = new UserFinancialData(userId, totalExpenses, totalIncomes, totalCards, balance);
			return Optional.of(data);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	private BigDecimal getTotalIncomes(long userId) {
		BigDecimal total = new BigDecimal(0);
		incomeService.findAllByUserId(userId).forEach(income -> {
			total.add(income.value());
		});

		return total;
	}

	private BigDecimal getTotalExpenses(long userId) {
		BigDecimal total = new BigDecimal(0);
		expenseService.findAllByUserId(userId)
				.forEach(expense -> total.add(expense.value()));
		return total;
	}
	
	private BigDecimal getTotalCards(long userId) {
		BigDecimal total = new BigDecimal(0);
		cardService.findAllByUserId(userId).forEach(card -> {
			if (card.type() == CardType.CREDIT.getValue()) {
				total.add(card.limit());
			} else if (card.type() == CardType.DEBIT.getValue()) {
				total.add(card.currentValue());
			}
		});
		
		return total;
	}
}
