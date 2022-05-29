package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.dao.CategoryDAO;
import pii.model.Category;

@Component
public class CategoryRepositoryImpl implements CategoryRepository {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Override
	public List<Category> findAll() {
		return categoryDAO.findAll();
	}
	
	@Override
	public Optional<Category> findById(Long id) {
		return categoryDAO.findById(id);
	}

	@Override
	public Optional<Category> findByName(String name) {
		return categoryDAO.findByName(name);
	}

	@Override
	public Optional<Category> save(Category category) {
		var id = categoryDAO.save(category); 
		
		if (id.isPresent()) {
			return categoryDAO.findById(id.get());
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<Category> update(Long id, Category category) {
		return categoryDAO.findById(id);
	}

	@Override
	public Boolean delete(Long id) {
		return categoryDAO.delete(id);
	}
}
