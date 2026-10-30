package com.openclassrooms.chatop.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.chatop.dto.MessageResponse;
import com.openclassrooms.chatop.dto.RentalRequest;
import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.dto.RentalsResponse;
import com.openclassrooms.chatop.service.RentalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

	private final RentalService rentalService;

	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	@GetMapping
	public RentalsResponse getAll() {
		return new RentalsResponse(rentalService.getAll());
	}

	@GetMapping("/{id}")
	public RentalResponse getById(@PathVariable Integer id) {
		return rentalService.getById(id);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public MessageResponse create(@Valid @ModelAttribute RentalRequest request,
			@RequestParam MultipartFile picture, Authentication authentication) {
		rentalService.create(request, picture, authentication.getName());
		return new MessageResponse("Rental created !");
	}

	@PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public MessageResponse update(@PathVariable Integer id, @Valid @ModelAttribute RentalRequest request,
			Authentication authentication) {
		rentalService.update(id, request, authentication.getName());
		return new MessageResponse("Rental updated !");
	}

}
