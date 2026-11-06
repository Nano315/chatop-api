package com.openclassrooms.chatop.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatop.dto.ErrorResponse;
import com.openclassrooms.chatop.dto.MessageRequest;
import com.openclassrooms.chatop.dto.MessageResponse;
import com.openclassrooms.chatop.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Messages sent to rental owners")
public class MessageController {

	private final MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * Saves a message sent to the owner of a rental.
	 */
	@PostMapping
	@Operation(summary = "Send a message to the owner of a rental")
	@ApiResponse(responseCode = "200", description = "Message sent")
	@ApiResponse(responseCode = "400", description = "Missing field, or unknown rental / user",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content)
	public MessageResponse send(@Valid @RequestBody MessageRequest request) {
		messageService.send(request);
		return new MessageResponse("Message send with success");
	}

}
