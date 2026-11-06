package com.openclassrooms.chatop.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.chatop.model.User;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
		Integer id,
		String name,
		String email,
		@JsonProperty("created_at") @JsonFormat(pattern = "yyyy/MM/dd")
		@Schema(type = "string", example = "2026/01/15") LocalDateTime createdAt,
		@JsonProperty("updated_at") @JsonFormat(pattern = "yyyy/MM/dd")
		@Schema(type = "string", example = "2026/01/15") LocalDateTime updatedAt) {

	public static UserResponse from(User user) {
		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
	}

}
