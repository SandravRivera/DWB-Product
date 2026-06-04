package com.product.api.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) para la salida de datos de elementos del carrito.
 * Se utiliza para capturar y validar los datos enviados por el cliente.
 * en las operaciones de creación (POST) y actualización (PUT).
 */
public class DtoCartItemOut {
 
    /**
     * Identificador único del elemento del carrito.
     * Restricción: No puede ser nulo.
     */
    @JsonProperty("cart_item_id")
    @NotNull(message = "Es obligatorio mostrar el cart_item_id")
    private Integer cartItemId;
    
    
    /**
     * Identificador del producto que se está comprando.
     * Restricción: No puede ser nulo.
     */
    @JsonProperty("product_id")
    @NotNull(message = "Es obligatorio mostrar el product_id")
    private Integer productId;
    
    
    /**
     * Cantidad de unidades del producto que se está comprando.
     * Restricción: No puede ser nulo ni menor a cero.
     */
    @JsonProperty("quantity")
    @Min(value = 0)
    @NotNull(message = "Es obligatorio mostrar el quantity")
    private Integer quantity;
    
    
    /**
     * Precio total de la compra.
     * Restricción: No puede ser nulo ni menor a cero.
     */
    @JsonProperty("price")
    @Min(value = 0)
    @NotNull(message = "Es obligatorio mostrar el price")
    private Float price;

    public DtoCartItemOut(Integer cartItemId, Integer productId, Integer quantity, Float price) {
        super();
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    /******************
     * GETTERS
     ******************/

    public Integer getCartItemId() {
        return cartItemId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Float getPrice() {
        return price;
    }


    /******************
     * SETTERS
     ******************/

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

}
