package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pii.model.Category;

@Repository
public interface CategoryRepository {

	public List<Category> findAll();
	
	public Optional<Category> findById(Long id);
	
	public Optional<Category> findByName(String name);
	
	public Optional<Category> save(Category category);
	
	public Optional<Category> update(Long id, Category category);
	
	public Boolean delete(Long id);
}
