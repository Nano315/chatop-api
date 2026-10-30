package com.openclassrooms.chatop.service;

import org.springframework.stereotype.Service;

import com.openclassrooms.chatop.dto.MessageRequest;
import com.openclassrooms.chatop.exception.BadRequestException;
import com.openclassrooms.chatop.model.Message;
import com.openclassrooms.chatop.repository.MessageRepository;
import com.openclassrooms.chatop.repository.RentalRepository;
import com.openclassrooms.chatop.repository.UserRepository;

@Service
public class MessageService {

	private final MessageRepository messageRepository;
	private final RentalRepository rentalRepository;
	private final UserRepository userRepository;

	public MessageService(MessageRepository messageRepository, RentalRepository rentalRepository,
			UserRepository userRepository) {
		this.messageRepository = messageRepository;
		this.rentalRepository = rentalRepository;
		this.userRepository = userRepository;
	}

	public void send(MessageRequest request) {
		Message message = new Message();
		message.setRental(rentalRepository.findById(request.rentalId())
				.orElseThrow(() -> new BadRequestException("Rental not found")));
		message.setUser(userRepository.findById(request.userId())
				.orElseThrow(() -> new BadRequestException("User not found")));
		message.setMessage(request.message());
		messageRepository.save(message);
	}

}
