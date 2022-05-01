package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pii.model.Card;

@Repository
public interface CardRepository {

	public List<Card> findAll();

	public List<Card> findAllByUserId(Long userId);

	public Optional<Card> findById(Long id);

	public Optional<Card> findByNumber(Long number);

	public Optional<Card> save(Card card);

	public Optional<Card> update(Long id, Card card);

	public Boolean delete(Long id);
}
