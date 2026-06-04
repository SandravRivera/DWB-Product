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
     * Identificador del producto a agregar.
     * Mapeado desde el campo JSON "product_id".
     * Restricción: No puede ser nulo.
     */
    @JsonProperty("product_id")
    @NotNull(message = "Es obligatorio ingresar el product_id")
    private Integer productId;
    
    /**
     * Cantidad de unidades del producto que se está comprando.
     * Mapeado desde el campo JSON "number".
     * Restricción: No puede ser nulo ni menor a uno.
     */
    @JsonProperty("number")
    @Min(value = 1)
    @NotNull(message = "Es obligatorio ingresar el number")
    private Integer number;


    /******************
     * GETTERS
     ******************/

    public Integer getProductId() {
        return productId;
    }

    public Integer getNumber() {
        return number;
    }


    /******************
     * SETTERS
     ******************/

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}
