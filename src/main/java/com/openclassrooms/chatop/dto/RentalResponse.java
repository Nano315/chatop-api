package com.openclassrooms.chatop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openclassrooms.chatop.model.Rental;

public record RentalResponse(
		Integer id,
		String name,
		BigDecimal surface,
		BigDecimal price,
		String picture,
		String description,
		Integer ownerId,
		@JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime createdAt,
		@JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime updatedAt) {

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
