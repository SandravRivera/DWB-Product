package com.product.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.api.dto.in.DtoCartItemIn;
import com.product.api.dto.out.DtoCartItemOut;
import com.product.api.service.SvcCartItem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión del carrito de compras.
 * Proporciona endpoints para operaciones CRUD.
 * Maneja excepciones globales a través de {@code RestExceptionHandler} 
 * para devolver respuestas estructuradas en caso de error.
 */
@RestController
@RequestMapping("/cart-item")
@Tag(name = "Cart Item", description = "Carrito de compras")
public class CtrlCartItem {

    @Autowired
    SvcCartItem svc;
	
	@PostMapping
	@Operation(summary = "Agregar un producto al carrito", description = "Agrega un producto o aumenta la candidad del carrito de compras")
	public ResponseEntity<String> addCartItem(@Valid @RequestBody DtoCartItemIn in){
		return svc.addCartItem(in);
	}
	
	@GetMapping
	@Operation(summary = "Consultar productos del carrito", description = "Lista los productos registrados en el carrito de compras")
	public ResponseEntity<List<DtoCartItemOut>> getCartItems(){
		return svc.getCartItems();
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar un producto del carrito", description = "Borra definitivamente un producto del carrito")
	public ResponseEntity<String> deleteCartItem(@PathVariable Integer id){
		return svc.deleteCartItem(id);
	}
	
	@DeleteMapping
	@Operation(summary = "Eliminar todos los productos del carrito", description = "Borra definitivamente todos los productos del carrito")
	public ResponseEntity<String> deleteCartItems(){
		return svc.deleteCartItems();
	}
}
