package pii.dto;

import org.springframework.stereotype.Component;

import pii.model.Card;

@Component
public class CardDTOMapper {
	
	public CardDTO toDTO(Card card) {
		return new CardDTO(
				card.id(),
				card.userId(),
				card.number(),
				card.type().getValue(),
				card.brand(),
				card.limit(),
				card.currentValue(),
				card.dueDate());
	}
	
	public Card dtoToObj(CardDTO dto) {
		return new Card(
				dto.id(),
				dto.userId(),
				dto.number(),
				dto.type(),
				dto.brand(),
				dto.limit(),
				dto.currentValue(),
				dto.dueDate());
	}
}
