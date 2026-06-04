package com.product.common.mapper;

import org.springframework.stereotype.Service;

import com.product.api.dto.in.DtoCartItemIn;
import com.product.api.dto.out.DtoCartItemOut;
import com.product.api.entity.CartItem;

@Service
public class MapperCartItem {
	
	public DtoCartItemOut fromCartItem(CartItem item, float price){
		DtoCartItemOut dto = new DtoCartItemOut(
			item.getCartItemId(),
			item.getProductId(),
			item.getQuantity(),
			price
		);
		return dto;
	}

	// Nuevo cart item
	public CartItem fromDto(DtoCartItemIn dto, Integer userId) {
		CartItem item = new CartItem();
		item.setUserId(userId);
		item.setProductId(dto.getProductId());
		item.setQuantity(dto.getQuantity());
        return item;
	}

	// Actualizar cart item
	public CartItem fromDto(DtoCartItemIn dto, Integer cartItemId, Integer userId) {
		CartItem item = fromDto(dto, userId);
		item.setCartItemId(cartItemId);
        return item;
	}
	
}
