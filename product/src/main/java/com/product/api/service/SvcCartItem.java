package com.product.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.in.DtoCartItemIn;
import com.product.api.dto.out.DtoCartItemOut;

/**
 * Interfaz de servicio para la gestión de artículos del carrito.
 */
public interface SvcCartItem {

	public ResponseEntity<String> addCartItem(DtoCartItemIn in);
	public ResponseEntity<List<DtoCartItemOut>> getCartItems();
	public ResponseEntity<String> deleteCartItem(Integer id);
	public ResponseEntity<String> deleteCartItems();
}