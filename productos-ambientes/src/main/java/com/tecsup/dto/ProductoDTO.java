package com.tecsup.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProductoDTO {

	@NotBlank(message = "El nombre no puede estar vacío")
	private String nombre;

	@Positive(message = "El precio debe ser mayor que cero")
	private double precio;

	@Min(value = 0, message = "El stock no puede ser negativo")
	private int stock;

	@NotBlank(message = "La categoría no puede estar vacía")
	private String categoria;

	public ProductoDTO() {
	}

	public ProductoDTO(String nombre, double precio, int stock, String categoria) {
		this.nombre = nombre;
		this.precio = precio;
		this.stock = stock;
		this.categoria = categoria;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
}