package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pii.model.Income;

@Repository
public interface IncomeRepository {
	
	public List<Income> findAll();
	
	public List<Income> findAllByUserId(Long userId);
	
	public List<Income> findAllByCategoryId(Long categoryId);
	
	public Optional<Income> findById(Long id);
	
	public Optional<Income> save(Income income);
	
	public Optional<Income> update(Long id, Income income);
	
	public Boolean delete(Long id);
}
