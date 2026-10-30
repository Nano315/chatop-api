package com.openclassrooms.chatop.service;

import org.springframework.stereotype.Service;

import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.exception.ResourceNotFoundException;
import com.openclassrooms.chatop.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserResponse getById(Integer id) {
		return userRepository.findById(id)
				.map(UserResponse::from)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	public UserResponse getByEmail(String email) {
		return userRepository.findByEmail(email)
				.map(UserResponse::from)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

}
