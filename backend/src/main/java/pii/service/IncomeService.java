package pii.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.IncomeDTO;
import pii.dto.IncomeDTOMapper;
import pii.repository.IncomeRepository;

@Service
public class IncomeService {
	
	@Autowired
	private IncomeRepository repository;
	
	@Autowired
	private IncomeDTOMapper incomeDTOMapper;
	
	public List<IncomeDTO> findAll() {
		return repository
				.findAll()
				.stream()
				.map(incomeDTOMapper::toDTO)
				.toList();
	}
	
	public List<IncomeDTO> findAllByUserId(Long userId) {
		return repository
				.findAllByUserId(userId)
				.stream()
				.map(incomeDTOMapper::toDTO)
				.toList();
	}
	
	public List<IncomeDTO> findAllByCategoryId(Long categoryId) {
		return repository
				.findAllByCategoryId(categoryId)
				.stream()
				.map(incomeDTOMapper::toDTO)
				.toList();
	}
	
	public Optional<IncomeDTO> findById(Long id) {
		return repository
				.findById(id)
				.map(incomeDTOMapper::toDTO);
	}
	
	public Optional<IncomeDTO> save(IncomeDTO incomeDTO) {
		var income = incomeDTOMapper.toObj(incomeDTO);
		
		return repository
				.save(income)
				.map(incomeDTOMapper::toDTO);
	}
	
	public Optional<IncomeDTO> update(Long id, IncomeDTO incomeDTO) {
		var income = incomeDTOMapper.toObj(incomeDTO);
		
		return repository
				.update(id, income)
				.map(incomeDTOMapper::toDTO);
	}
	
	public Boolean delete(Long id) {
		return repository.delete(id);
	}
}
