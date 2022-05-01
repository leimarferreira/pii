package pii.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public record CardDTO(
		Long id,
		@JsonProperty("user_id")
		Long userId,
		Long number,
		Integer type,
		String brand,
		BigDecimal limit,
		@JsonProperty("current_value")
		BigDecimal currentValue,
		@JsonProperty("due_date")
		Integer dueDate
) {}
