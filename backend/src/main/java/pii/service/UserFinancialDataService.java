package pii.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.UserFinancialData;
import pii.enums.PaymentMethod;
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
			var debitExpenses = getDebitExpenses(userId);
			var creditExpenses = getCreditExpenses(userId);
			var moneyExpenses = getMoneyExpenses(userId);
			var totalCredit = getTotalCredit(userId);
			var totalExpenses = getTotalExpenses(userId);
			
			var balance = new BigDecimal(0);
			balance = balance.add(totalIncomes);
			balance = balance.subtract(debitExpenses);
			balance = balance.add(totalCredit);
			
			var data = new UserFinancialData(
					userId,
					creditExpenses,
					debitExpenses,
					moneyExpenses,
					totalExpenses,
					totalIncomes,
					totalCredit,
					balance);
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
	
	private BigDecimal getDebitExpenses(long userId) {
		var total = new BigDecimal(0);
		var expenses = expenseRepository.findAllByUserId(userId)
				.stream()
				.filter(expense -> expense.paymentMethod() == PaymentMethod.DEBIT)
				.toList();
		for (var expense : expenses) {
			total = total.add(expense.value());
		}
		
		return total;
	}
	
	private BigDecimal getCreditExpenses(long userId) {
		var total = new BigDecimal(0);
		var expenses = expenseRepository.findAllByUserId(userId)
				.stream()
				.filter(expense -> expense.paymentMethod() == PaymentMethod.CREDIT)
				.toList();
		for (var expense : expenses) {
			total = total.add(expense.value());
		}
		
		return total;
	}
	
	private BigDecimal getMoneyExpenses(long userId) {
		var total = new BigDecimal(0);
		var expenses = expenseRepository.findAllByUserId(userId)
				.stream()
				.filter(expense -> expense.paymentMethod() == PaymentMethod.MONEY)
				.toList();
		for (var expense : expenses) {
			total = total.add(expense.value());
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
	
	private BigDecimal getTotalCredit(long userId) {
		var total = new BigDecimal(0);
		var cards = cardService.findAllByUserId(userId)
				.stream()
				.filter(card -> card.type() == 1)
				.toList();
		for (var card : cards) {
			total = total.add(card.limit().subtract(card.currentValue()));
		}
		
		return total;
	}
}
