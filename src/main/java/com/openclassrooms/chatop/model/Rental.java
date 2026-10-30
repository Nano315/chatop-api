package com.openclassrooms.chatop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rentals")
public class Rental {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private BigDecimal surface;

	private BigDecimal price;

	private String picture;

	private String description;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_id")
	private User owner;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSurface() {
		return surface;
	}

	public void setSurface(BigDecimal surface) {
		this.surface = surface;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

}
