package com.product.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

/**
 * Entidad que representa la tabla "cart_item" en la base de datos.
 * Esta clase mapea los objetos de Java con los registros de la base de datos 
 * y define el formato de salida JSON para las respuestas de la API.
 */
@Entity
@Table(name="cart_item")
public class CartItem {
    /**
     * Identificador único del artículo en el carrito de compras.
     * Mapeado como llave primaria con estrategia de auto-incremento (IDENTITY).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("cart_item_id")
    @Column(name = "cart_item_id")
    private Integer cartItemId;

    /**
     * Identificador del usuario al que pertenece.
     */
    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Integer userId;

    /**
     * Identificador del producto que se está comprando.
     */
    @JsonProperty("gtin")
    @Column(name = "gtin")
    private String gtin;

    /**
     * Cantidad del producto que se está comprando.
     */
    @JsonProperty("quantity")
    @Column(name = "quantity")
    private Integer quantity;

    

    public CartItem() {
    }

    public CartItem(Integer cartItemId, Integer userId, String gtin, Integer quantity) {
        super();
        this.cartItemId = cartItemId;
        this.userId = userId;
        this.gtin = gtin;
        this.quantity = quantity;
    }

    /******************
     * GETTERS
     ******************/
    
    public Integer getCartItemId() {
        return cartItemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getGtin() {
        return gtin;
    }

    public Integer getQuantity() {
        return quantity;
    }


    /******************
     * SETTERS
     ******************/

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
