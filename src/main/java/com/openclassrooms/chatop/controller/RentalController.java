package com.openclassrooms.chatop.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.chatop.dto.ErrorResponse;
import com.openclassrooms.chatop.dto.MessageResponse;
import com.openclassrooms.chatop.dto.RentalRequest;
import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.dto.RentalsResponse;
import com.openclassrooms.chatop.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "Rentals listing, creation and update")
public class RentalController {

	private final RentalService rentalService;

	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	/**
	 * Returns all the rentals.
	 */
	@GetMapping
	@Operation(summary = "List all rentals")
	@ApiResponse(responseCode = "200", description = "All rentals")
	@ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content)
	public RentalsResponse getAll() {
		return new RentalsResponse(rentalService.getAll());
	}

	/**
	 * Returns one rental (404 if it does not exist).
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Get a rental")
	@ApiResponse(responseCode = "200", description = "The rental")
	@ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content)
	@ApiResponse(responseCode = "404", description = "Rental not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public RentalResponse getById(@Parameter(description = "Rental id") @PathVariable Integer id) {
		return rentalService.getById(id);
	}

	/**
	 * Creates a rental from the multipart form, owned by the authenticated user, and saves its picture.
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Create a rental", description = "The authenticated user becomes the owner of the rental.")
	// springdoc does not merge the picture file with the form fields: the multipart body is described explicitly
	@RequestBody(content = @Content(
			mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
			schemaProperties = {
					@SchemaProperty(name = "name", schema = @Schema(type = "string", requiredMode = RequiredMode.REQUIRED)),
					@SchemaProperty(name = "surface", schema = @Schema(type = "number", requiredMode = RequiredMode.REQUIRED)),
					@SchemaProperty(name = "price", schema = @Schema(type = "number", requiredMode = RequiredMode.REQUIRED)),
					@SchemaProperty(name = "description", schema = @Schema(type = "string", requiredMode = RequiredMode.REQUIRED)),
					@SchemaProperty(name = "picture", schema = @Schema(type = "string", format = "binary", requiredMode = RequiredMode.REQUIRED))
			}))
	@ApiResponse(responseCode = "200", description = "Rental created")
	@ApiResponse(responseCode = "400", description = "Missing or invalid field, or picture missing / not an image",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content)
	public MessageResponse create(@Valid @ModelAttribute RentalRequest request, @RequestPart MultipartFile picture,
			Authentication authentication) {
		rentalService.create(request, picture, authentication.getName());
		return new MessageResponse("Rental created !");
	}

	/**
	 * Updates the text fields of a rental. Only its owner can do it (403 otherwise).
	 */
	@PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Update a rental", description = "Only the owner of the rental can update it. The picture is not changed.")
	@ApiResponse(responseCode = "200", description = "Rental updated")
	@ApiResponse(responseCode = "400", description = "Missing or invalid field",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content)
	@ApiResponse(responseCode = "403", description = "The authenticated user is not the owner",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "Rental not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public MessageResponse update(@Parameter(description = "Rental id") @PathVariable Integer id,
			@Valid @ModelAttribute RentalRequest request, Authentication authentication) {
		rentalService.update(id, request, authentication.getName());
		return new MessageResponse("Rental updated !");
	}

}
