package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pii.model.Expense;

@Repository
public interface ExpenseRepository {

	public List<Expense> findAll();
	
	public List<Expense> findAllByUserId(Long userId);
	
	public List<Expense> findAllByCategoryId(Long categoryId);
	
	public Optional<Expense> findById(Long id);
	
	public Optional<Expense> save(Expense expense);
	
	public Optional<Expense> update(Long id, Expense expense);
	
	public Boolean delete(Long id);
}
