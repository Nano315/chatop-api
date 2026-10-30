package com.openclassrooms.chatop.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.chatop.exception.BadRequestException;

/**
 * Saves uploaded pictures in the upload folder and returns the public URL to display them.
 */
@Service
public class ImageStorageService {

	private final Path uploadDir;
	private final String imagesBaseUrl;

	public ImageStorageService(@Value("${app.upload-dir}") String uploadDir,
			@Value("${app.images-base-url}") String imagesBaseUrl) {
		this.uploadDir = Path.of(uploadDir).toAbsolutePath().normalize();
		this.imagesBaseUrl = imagesBaseUrl;
	}

	public String store(MultipartFile picture) {
		if (picture.isEmpty() || picture.getContentType() == null || !picture.getContentType().startsWith("image/")) {
			throw new BadRequestException("picture must be an image");
		}
		// A random name avoids collisions and never reuses the name chosen by the client
		String extension = StringUtils.getFilenameExtension(picture.getOriginalFilename());
		String filename = UUID.randomUUID() + (extension != null && extension.matches("[A-Za-z0-9]{1,10}") ? "." + extension : "");
		try {
			Files.createDirectories(uploadDir);
			picture.transferTo(uploadDir.resolve(filename));
		} catch (IOException exception) {
			throw new UncheckedIOException("Could not store the picture", exception);
		}
		return imagesBaseUrl + filename;
	}

}
