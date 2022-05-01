package pii.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.CardDTO;
import pii.dto.CardDTOMapper;
import pii.repository.CardRepository;

@Service
public class CardService {

	@Autowired
	private CardRepository repository;

	@Autowired
	private CardDTOMapper mapper;

	public List<CardDTO> findAll() {
		return repository.findAll()
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}

	public List<CardDTO> findAllByUserId(Long userId) {
		return repository.findAllByUserId(userId)
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}

	public Optional<CardDTO> findById(Long id) {
		return repository.findById(id).map(mapper::toDTO);
	}

	public Optional<CardDTO> findByNumber(Long number) {
		return repository.findByNumber(number).map(mapper::toDTO);
	}

	public Optional<CardDTO> save(CardDTO dto) {
		var card = mapper.dtoToObj(dto);
		
		return repository.save(card).map(mapper::toDTO);
	}

	public Optional<CardDTO> update(Long id, CardDTO dto) {
		var card = mapper.dtoToObj(dto);
		
		return repository.update(id, card).map(mapper::toDTO);
	}

	public boolean delete(Long id) {
		return repository.delete(id);
	}
}
