package com.openclassrooms.chatop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.chatop.model.Rental;

import io.swagger.v3.oas.annotations.media.Schema;

public record RentalResponse(
		Integer id,
		String name,
		BigDecimal surface,
		BigDecimal price,
		String picture,
		String description,
		@JsonProperty("owner_id") Integer ownerId,
		@JsonProperty("created_at") @JsonFormat(pattern = "yyyy/MM/dd")
		@Schema(type = "string", example = "2026/01/15") LocalDateTime createdAt,
		@JsonProperty("updated_at") @JsonFormat(pattern = "yyyy/MM/dd")
		@Schema(type = "string", example = "2026/01/15") LocalDateTime updatedAt) {

	public static RentalResponse from(Rental rental) {
		return new RentalResponse(
				rental.getId(),
				rental.getName(),
				rental.getSurface(),
				rental.getPrice(),
				rental.getPicture(),
				rental.getDescription(),
				rental.getOwner().getId(),
				rental.getCreatedAt(),
				rental.getUpdatedAt());
	}

}
