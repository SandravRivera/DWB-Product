package com.product.api.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoCategoryIn {
    @JsonProperty("category")
    @NotNull(message = "La categoría es obligatoria")
    private String category;

    @JsonProperty("tag")
    @NotNull(message = "El tag es obligatorio")
    private String tag;

    /******************
     * SETTERS
     ******************/
    public void setCategory(String category) {
        this.category = category;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    /******************
     * GETTERS
     ******************/
    public String getCategory() {
        return category;
    }
    public String getTag() {
        return tag;
    }

    
    
}
