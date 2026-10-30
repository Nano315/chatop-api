package com.openclassrooms.chatop.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.chatop.dto.RentalRequest;
import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.exception.ResourceNotFoundException;
import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.RentalRepository;
import com.openclassrooms.chatop.repository.UserRepository;

@Service
public class RentalService {

	private final RentalRepository rentalRepository;
	private final UserRepository userRepository;
	private final ImageStorageService imageStorageService;

	public RentalService(RentalRepository rentalRepository, UserRepository userRepository,
			ImageStorageService imageStorageService) {
		this.rentalRepository = rentalRepository;
		this.userRepository = userRepository;
		this.imageStorageService = imageStorageService;
	}

	@Transactional(readOnly = true)
	public List<RentalResponse> getAll() {
		return rentalRepository.findAll().stream()
				.map(RentalResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public RentalResponse getById(Integer id) {
		return RentalResponse.from(findRental(id));
	}

	/**
	 * Creates a rental owned by the authenticated user, with the uploaded picture.
	 */
	public void create(RentalRequest request, MultipartFile picture, String ownerEmail) {
		User owner = userRepository.findByEmail(ownerEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		Rental rental = new Rental();
		rental.setOwner(owner);
		rental.setPicture(imageStorageService.store(picture));
		applyRequest(rental, request);
		rentalRepository.save(rental);
	}

	/**
	 * Updates the text fields of a rental. Only its owner is allowed to do it.
	 */
	@Transactional
	public void update(Integer id, RentalRequest request, String userEmail) {
		Rental rental = findRental(id);
		if (!rental.getOwner().getEmail().equals(userEmail)) {
			throw new AccessDeniedException("Only the owner can update this rental");
		}
		applyRequest(rental, request);
		rentalRepository.save(rental);
	}

	private Rental findRental(Integer id) {
		return rentalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Rental not found"));
	}

	private void applyRequest(Rental rental, RentalRequest request) {
		rental.setName(request.name());
		rental.setSurface(request.surface());
		rental.setPrice(request.price());
		rental.setDescription(request.description());
	}

}
