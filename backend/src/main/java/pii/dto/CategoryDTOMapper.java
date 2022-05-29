package pii.dto;

import org.springframework.stereotype.Component;

import pii.model.Category;

@Component
public class CategoryDTOMapper {
	public CategoryDTO toDTO(Category category) {
		return new CategoryDTO(category.id(), category.name());
	}
	
	public Category toObj(CategoryDTO dto) {
		return new Category(dto.id(), dto.name());
	}
}
