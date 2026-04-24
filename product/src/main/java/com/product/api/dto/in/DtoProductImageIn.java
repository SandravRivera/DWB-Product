package com.product.api.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public class DtoProductImageIn {
    
    @JsonProperty("product_id")
    @NotNull(message = "Es obligatorio ingresar product_id")
    private Integer productId;

    @JsonProperty("image")
    @NotNull(message = "Es obligatorio ingresar image")
    private String image;


    /******************
     * GETTERS
     ******************/

    public Integer getProductId() {
        return productId;
    }

    public String getImage() {
        return image;
    }


    /******************
     * SETTERS
     ******************/

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
