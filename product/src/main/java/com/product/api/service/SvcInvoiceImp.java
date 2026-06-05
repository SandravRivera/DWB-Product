package com.product.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.product.api.dto.ApiResponse;
import com.product.api.dto.DtoInvoiceList;
import com.product.api.entity.CartItem;
import com.product.api.entity.Invoice;
import com.product.api.entity.InvoiceItem;
import com.product.api.entity.Product;
import com.product.api.repository.RepoCartItem;
import com.product.api.repository.RepoInvoice;
import com.product.api.repository.RepoInvoiceItem;
import com.product.api.repository.RepoProduct;
import com.product.common.mapper.MapperInvoice;
import com.product.common.util.JwtDecoder;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;

@Service
public class SvcInvoiceImp implements SvcInvoice {
	
	@Autowired
    private RepoInvoice repo;
	
	@Autowired
	RepoCartItem repoCartItem;
	
	@Autowired
	RepoProduct repoProduct;

	@Autowired
	RepoInvoiceItem repoInvoiceItem;

	@Autowired
	private JwtDecoder jwtDecoder;
	
	@Autowired
	MapperInvoice mapper;

	Double TAX_PERCENTAGE = 0.16;

	@Override
	public List<DtoInvoiceList> findAll() {
		try {
			if(jwtDecoder.isAdmin()) {
				return mapper.toDtoList(repo.findAll());
			}else {
				Integer user_id = jwtDecoder.getUserId();
				return mapper.toDtoList(repo.findAllByUserId(user_id));
			}
		}catch (DataAccessException e) {
	        throw new DBAccessException();
	    }
	}

	@Override
	public Invoice findById(Integer id) {
		try {
			Invoice invoice = repo.findById(id).get();
			if(!jwtDecoder.isAdmin()) {
				Integer user_id = jwtDecoder.getUserId();
				if(invoice.getUser_id() != user_id) {
					throw new ApiException(HttpStatus.FORBIDDEN, "El token no es válido para consultar esta factura");
				}
			}
			return invoice;
		}catch (DataAccessException e) {
	        throw new DBAccessException();
	    }catch (NoSuchElementException e) {
			throw new ApiException(HttpStatus.NOT_FOUND, "El id de la factura no existe");
	    }
	}

	@Override
	public ApiResponse create() {
		try {
			// 1. Identifique los artículos agregados por el cliente.
			Integer userId = jwtDecoder.getUserId();
			List<CartItem> cartItems = repoCartItem.findByUserId(userId);
			// Vacío, no proceder con la factura
			if(cartItems.isEmpty())
				throw new ApiException(HttpStatus.BAD_REQUEST, "El carrito está vacío");
			// 2. Valide que los productos tienen stock suficiente.
			for(CartItem cartItem: cartItems) {
				String gtin = cartItem.getGtin();
				// Encontrar producto
				Product product = repoProduct.findByGtin(gtin)
					.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "El producto con GTIN " + gtin + " no existe"));
				// Validar stock
				if(product.getStock() < cartItem.getQuantity()) 
					throw new ApiException(HttpStatus.CONFLICT, "El producto con GTIN " + gtin + " no tiene artículos suficientes");
				// Validar estado
				if (product.getStatus() == 0) 
					throw new ApiException(HttpStatus.NOT_FOUND, "El producto con GTIN " + gtin + " no está disponible");
			}
			// 3. Calcule los totales de la compra
			// 4. Guarde info de compra (invoice) y productos comprados (invoice_item) en BD.
			// 5. Reste en el stock de los productos la cantidad de artículos comprados
			Double subtotal = 0.0, total = 0.0, taxes = 0.0;
			Invoice invoice = new Invoice();
			List<InvoiceItem> items = new ArrayList<>();
			for(CartItem cartItem: cartItems) {
				// Encontrar producto
				Product product = repoProduct.findByGtin(cartItem.getGtin()).get();
				// Agregar totales del artículo
				Double itemTotal = product.getPrice() * (double)cartItem.getQuantity();
				Double itemTaxes = itemTotal * TAX_PERCENTAGE;
				Double itemSubtotal = itemTotal - itemTaxes;
				total += itemTotal;
				taxes += itemTaxes;
				subtotal += itemSubtotal;
				// Guardar producto de la factura (invoice_item)
				InvoiceItem invItem = new InvoiceItem();
				invItem.setInvoice_id(invoice.getInvoice_id());				
				invItem.setGtin(cartItem.getGtin());
				invItem.setQuantity(cartItem.getQuantity());
				invItem.setUnit_price((double)product.getPrice());
				invItem.setSubtotal(itemSubtotal);
				invItem.setTaxes(itemTaxes);
				invItem.setTotal(itemTotal);
				repoInvoiceItem.save(invItem);
				items.add(invItem);
				// Restar del stock
				product.setStock(product.getStock() - cartItem.getQuantity());
				repoProduct.save(product);
			}
			// Guardar info de compra (invoice)
			invoice.setUser_id(userId);
			invoice.setCreated_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			invoice.setSubtotal(subtotal);
			invoice.setTaxes(taxes);
			invoice.setTotal(total);
			invoice.setItems(items);
			repo.save(invoice);
			// 6. Vacíe el carrito de compras del cliente.
			repoCartItem.deleteByUserId(userId);
			return new ApiResponse("La factura ha sido registrada");
		}catch (DataAccessException e) {
	        throw new DBAccessException();
	    }
	}
}
