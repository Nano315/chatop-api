package com.openclassrooms.chatop.configuration;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Serves the uploaded rental pictures under /images/**.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final String uploadLocation;

	public WebConfig(@Value("${app.upload-dir}") String uploadDir) {
		String location = Path.of(uploadDir).toAbsolutePath().normalize().toUri().toString();
		this.uploadLocation = location.endsWith("/") ? location : location + "/";
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").addResourceLocations(uploadLocation);
	}

}
