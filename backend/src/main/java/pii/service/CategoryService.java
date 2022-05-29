package pii.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.CategoryDTO;
import pii.dto.CategoryDTOMapper;
import pii.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryDTOMapper dtoMapper;

	public List<CategoryDTO> findAll() {
		return categoryRepository
				.findAll()
				.stream()
				.map(dtoMapper::toDTO)
				.toList();
	}

	public Optional<CategoryDTO> findById(Long id) {
		return categoryRepository
				.findById(id)
				.map(dtoMapper::toDTO);
	}

	public Optional<CategoryDTO> findByName(String name) {
		return categoryRepository
				.findByName(name)
				.map(dtoMapper::toDTO);
	}

	public Optional<CategoryDTO> save(CategoryDTO categoryDTO) {
		var category = dtoMapper.toObj(categoryDTO);
		
		return categoryRepository
				.save(category)
				.map(dtoMapper::toDTO);
	}

	public Optional<CategoryDTO> update(Long id, CategoryDTO categoryDTO) {
		var category = dtoMapper.toObj(categoryDTO);
		
		return categoryRepository
				.update(id, category)
				.map(dtoMapper::toDTO);
	}

	public Boolean delete(Long id) {
		return categoryRepository.delete(id);
	}
}