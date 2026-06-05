package com.product.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.product.api.dto.in.DtoCartItemIn;
import com.product.api.dto.out.DtoCartItemOut;
import com.product.api.entity.CartItem;
import com.product.api.entity.Product;
import com.product.api.repository.RepoCartItem;
import com.product.api.repository.RepoProduct;
import com.product.common.mapper.MapperCartItem;
import com.product.common.util.JwtDecoder;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;

@Service
public class SvcCartItemImp implements SvcCartItem {
	
	@Autowired
	RepoCartItem repo;
	
	@Autowired
	RepoProduct repoProduct;
	
	@Autowired
	MapperCartItem mapper;
	
	@Autowired
	private JwtDecoder jwtDecoder;

	public ResponseEntity<String> addCartItem(DtoCartItemIn in) {
		try {
			// Validar existencia del producto
			Product product = repoProduct.findByGtin(in.getGtin())
            	.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "El producto no existe"));
			// Buscar producto en el carrito actual
			Integer userId = jwtDecoder.getUserId();
			CartItem item = repo.findByGtinAndUserId(product.getGtin(), userId);
			// Si existe actualizar, si no crear
			if(item != null) item.setQuantity(item.getQuantity() + in.getQuantity());
			else item = mapper.fromDto(in, userId);
			// Verificar si hay productos suficientes
			if(product.getStock() < item.getQuantity()) {
				throw new ApiException(HttpStatus.CONFLICT, "No hay suficientes productos disponibles");
			}
			// Guardar y mandar mensaje de éxito
			repo.save(item);
			return new ResponseEntity<>("El artículo ha sido agregado", HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }

	public ResponseEntity<List<DtoCartItemOut>> getCartItems() {
		try {
            // Obtener los productos del usuario
			Integer userId = jwtDecoder.getUserId();
			List<CartItem> cartItems = repo.findByUserId(userId);
            // Mapearlos a DTO de salida
            List<DtoCartItemOut> list = new ArrayList<>();
            for(CartItem item: cartItems) {
                Float price = repoProduct.findByGtin(item.getGtin()).get().getPrice();
			    list.add(mapper.fromCartItem(item, price));
            }
            // Regresar lista de DTOs
            return new ResponseEntity<>(list, HttpStatus.OK);
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}

    }
	public ResponseEntity<String> deleteCartItem(Integer id) {
		try {
			// Validar existencia del artículo
			if (!repo.existsByCartItemIdAndUserId(id, jwtDecoder.getUserId())) {
				throw new ApiException(HttpStatus.NOT_FOUND, "El artículo no existe en este carrito.");
			}
			// Eliminar artículo de la base de datos
			CartItem item = repo.findById(id).get();
			repo.delete(item);
			// Regresar mensaje de operación finalizada correctamente
			return new ResponseEntity<>("El artículo ha sido eliminado", HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }
	
	public ResponseEntity<String> deleteCartItems() {
		try {
            // Obtener identificador del usuario
			Integer userId = jwtDecoder.getUserId();
			// Eliminar los artículos de la base de datos
            repo.deleteByUserId(userId);
			// Regresar mensaje de operación finalizada correctamente
			return new ResponseEntity<>("El carrito ha sido vaciado", HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }
}
