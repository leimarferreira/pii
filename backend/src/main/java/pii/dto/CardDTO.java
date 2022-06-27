package pii.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public record CardDTO(
		Long id,
		@JsonAlias(value = {"user_id", "userId"})
		Long userId,
		String number,
		Integer type,
		String brand,
		BigDecimal limit,
		@JsonAlias(value = {"current_value", "currentValue"})
		BigDecimal currentValue,
		@JsonAlias(value = {"due_date", "dueDate"})
		Integer dueDate
) {}
