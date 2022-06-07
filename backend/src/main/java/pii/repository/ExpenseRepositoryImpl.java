package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.dao.ExpenseDAO;
import pii.model.Expense;

@Component
public class ExpenseRepositoryImpl implements ExpenseRepository {

	@Autowired
	private ExpenseDAO expenseDAO;

	@Override
	public List<Expense> findAll() {
		return expenseDAO.findAll();
	}

	@Override
	public List<Expense> findAllByUserId(Long userId) {
		return expenseDAO.findAllByUserId(userId);
	}

	@Override
	public List<Expense> findAllByCategoryId(Long categoryId) {
		return expenseDAO.findAllByCategoryId(categoryId);
	}

	@Override
	public Optional<Expense> findById(Long id) {
		return expenseDAO.findById(id);
	}

	@Override
	public Optional<Expense> save(Expense expense) {
		var id = expenseDAO.save(expense);

		if (id.isPresent()) {
			return expenseDAO.findById(id.get());
		}

		return Optional.empty();
	}

	@Override
	public Optional<Expense> update(Long id, Expense expense) {
		expenseDAO.update(id, expense);
		return expenseDAO.findById(id);
	}

	@Override
	public Boolean delete(Long id) {
		return expenseDAO.delete(id);
	}

}
