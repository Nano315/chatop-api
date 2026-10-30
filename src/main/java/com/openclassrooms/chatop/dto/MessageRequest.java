package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageRequest(
		@NotNull Integer rentalId,
		@NotNull Integer userId,
		@NotBlank @Size(max = 2000) String message) {
}
