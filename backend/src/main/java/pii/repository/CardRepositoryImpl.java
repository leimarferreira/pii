package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.dao.CardDAO;
import pii.model.Card;

@Component
public class CardRepositoryImpl implements CardRepository {

	@Autowired
	private CardDAO cardDAO;

	@Override
	public List<Card> findAll() {
		return cardDAO.findAll();
	}

	@Override
	public List<Card> findAllByUserId(Long userId) {
		return cardDAO.findAllByUserId(userId);
	}

	@Override
	public Optional<Card> findById(Long id) {
		return cardDAO.findById(id);
	}

	@Override
	public Optional<Card> findByNumber(Long number) {
		return cardDAO.findByCardNumber(number);
	}

	@Override
	public Optional<Card> save(Card card) {
		var id = cardDAO.save(card);

		if (id.isPresent()) {
			return cardDAO.findById(id.get());
		}

		return Optional.empty();
	}

	@Override
	public Optional<Card> update(Long id, Card card) {
		cardDAO.update(id, card);
		return cardDAO.findById(id);
	}

	@Override
	public Boolean delete(Long id) {
		return cardDAO.delete(id);
	}

}
