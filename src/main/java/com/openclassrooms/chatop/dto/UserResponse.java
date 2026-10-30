package com.openclassrooms.chatop.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openclassrooms.chatop.model.User;

public record UserResponse(
		Integer id,
		String name,
		String email,
		@JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime createdAt,
		@JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime updatedAt) {

	public static UserResponse from(User user) {
		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
	}

}
