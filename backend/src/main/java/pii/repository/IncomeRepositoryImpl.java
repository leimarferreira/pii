package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.dao.IncomeDAO;
import pii.model.Income;

@Component
public class IncomeRepositoryImpl implements IncomeRepository {
	
	@Autowired
	private IncomeDAO incomeDAO;

	@Override
	public List<Income> findAll() {
		return incomeDAO.findAll();
	}

	@Override
	public List<Income> findAllByUserId(Long userId) {
		return incomeDAO.findAllByUserId(userId);
	}

	@Override
	public List<Income> findAllByCategoryId(Long categoryId) {
		return incomeDAO.findAllByCategoryId(categoryId);
	}

	@Override
	public Optional<Income> findById(Long id) {
		return incomeDAO.findById(id);
	}

	@Override
	public Optional<Income> save(Income income) {
		var result = incomeDAO.save(income);
		
		if (result.isPresent()) {
			return incomeDAO.findById(result.get());
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<Income> update(Long id, Income income) {
		incomeDAO.update(id, income);
		return incomeDAO.findById(id);
	}

	@Override
	public Boolean delete(Long id) {
		return incomeDAO.delete(id);
	}

}
