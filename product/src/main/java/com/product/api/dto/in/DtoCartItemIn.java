package com.product.api.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) para la entrada de datos de artículos en 
 * el carrito de compras de un usuario.
 * Se utiliza para capturar y validar los datos enviados por el cliente 
 * en las operaciones de creación (POST) y actualización (PUT).
 */
public class DtoCartItemIn {
    
    /**
     * GTIN del producto a agregar.
     * Mapeado desde el campo JSON "gtin".
     * Restricción: No puede ser nulo.
     */
    @JsonProperty("gtin")
    @NotNull(message = "Es obligatorio ingresar el gtin")
    private String gtin;
    
    /**
     * Cantidad de unidades del producto que se está comprando.
     * Mapeado desde el campo JSON "quantity".
     * Restricción: No puede ser nulo ni menor a uno.
     */
    @JsonProperty("quantity")
    @Min(value = 1)
    @NotNull(message = "Es obligatorio ingresar el quantity")
    private Integer quantity;


    /******************
     * GETTERS
     ******************/

    public String getGtin() {
        return gtin;
    }

    public Integer getQuantity() {
        return quantity;
    }


    /******************
     * SETTERS
     ******************/

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
