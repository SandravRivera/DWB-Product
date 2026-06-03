package com.product.api.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public class DtoProductImageOut {
    @JsonProperty("product_image_id")
    @NotNull(message = "Es obligatorio ingresar product_image_id")
    private Integer productImageId;
    
    @JsonProperty("product_id")
    @NotNull(message = "Es obligatorio ingresar product_id")
    private Integer productId;

    @JsonProperty("image")
    @NotNull(message = "Es obligatorio ingresar image")
    private String image;
    
    @JsonProperty("status")
    @NotNull(message = "Es obligatorio ingresar status")
    private Integer status;


    /******************
     * GETTERS
     ******************/

    public Integer getProductImageId() {
        return productImageId;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getImage() {
        return image;
    }

    public Integer getStatus() {
        return status;
    }


    /******************
     * SETTERS
     ******************/

    public void setProductImageId(Integer productImageId) {
        this.productImageId = productImageId;
    }
    
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    
    
}
