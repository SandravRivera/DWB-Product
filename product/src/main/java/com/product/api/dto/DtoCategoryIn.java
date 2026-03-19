package com.product.api.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object (DTO) para la entrada de datos de categorías.
 * Se utiliza para capturar y validar los datos enviados por el cliente 
 * en las operaciones de creación (POST) y actualización (PUT).
 */
public class DtoCategoryIn {

    /**
     * Nombre descriptivo de la categoría.
     * Mapeado desde el campo JSON "category".
     * Restricción: No puede ser nulo.
     */
    @JsonProperty("category")
    @NotNull(message = "La categoría es obligatoria")
    private String category;

    /**
     * Etiqueta única de la categoría.
     * Mapeado desde el campo JSON "tag".
     * Restricción: No puede ser nulo.
     */
    @JsonProperty("tag")
    @NotNull(message = "El tag es obligatorio")
    private String tag;

    /******************
     * SETTERS
     ******************/

    /**
     * Establece el nombre de la categoría.
     * @param category Nombre de la categoría.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Establece el tag de la categoría.
     * @param tag Etiqueta identificadora.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /******************
     * GETTERS
     ******************/

    /**
     * Obtiene el nombre de la categoría.
     * @return El nombre de la categoría.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Obtiene el tag de la categoría.
     * @return La etiqueta de la categoría.
     */
    public String getTag() {
        return tag;
    }
}