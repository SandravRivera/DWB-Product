package com.product.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

/**
 * Entidad que representa la tabla "category" en la base de datos.
 * Esta clase mapea los objetos de Java con los registros de la base de datos 
 * y define el formato de salida JSON para las respuestas de la API.
 */
@Entity
@Table(name="category")
public class Category {

    /**
     * Identificador único de la categoría.
     * Mapeado como llave primaria con estrategia de auto-incremento (IDENTITY).
     * En JSON se representa como "category_id".
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("category_id")
    @Column(name = "category_id")
    private int category_id;

    /**
     * Nombre descriptivo de la categoría.
     * Mapeado a la columna "category" de la base de datos.
     */
    @JsonProperty("category")
    @Column(name = "category")
    private String category;

    /**
     * Etiqueta identificadora única para la categoría.
     * Mapeado a la columna "tag" de la base de datos.
     */
    @JsonProperty("tag")
    @Column(name = "tag")
    private String tag;

    /**
     * Estado lógico de la categoría.
     * Habitualmente: 1 para activo, 0 para inactivo (borrado lógico).
     */
    @JsonProperty("status")
    @Column(name = "status")
    private Integer status;

    /**
     * Constructor protegido sin parámetros.
     * Requerido por el framework de persistencia (JPA/Hibernate) 
     * para la creación de instancias mediante reflexión.
     */
    protected Category() {}

    /**
     * Constructor para inicializar una instancia completa de la categoría.
     * @param category_id Identificador único.
     * @param category    Nombre de la categoría.
     * @param tag         Etiqueta identificadora.
     * @param status      Estado de la categoría.
     */
    public Category(Integer category_id, String category, String tag, Integer status) {
        this.category_id = category_id;
        this.category = category;
        this.tag = tag;
        this.status = status;
    }

    /**
     * Devuelve una representación en cadena de la categoría.
     * Útil para procesos de depuración (debugging) y logs.
     * @return String con el formato {id, 'nombre', 'tag', estado}.
     */
    @Override
    public String toString() {
        return "{" + category_id + ", '" + category + "', '" + tag + "', " + status + "}";
    }

    /******************
     * SETTERS
     ******************/

    /**
     * Establece el identificador de la categoría.
     * @param category_id identificador de la categoría.
     */
    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    /**
     * Establece el nombre de la categoría.
     * @param category Nombre de la categoría.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Establece la etiqueta de la categoría.
     * @param tag Etiqueta de la categoría.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Establece el estatus de la categoría.
     * @param status Estatus de la categoría.
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /******************
     * GETTERS
     ******************/
    
    /**
     * Obtiene el identificador de la categoría.
     * @return El identificador de la categoría.
     */
    public Integer getCategory_id() {
        return category_id;
    }

    /**
     * Obtiene el nombre de la categoría.
     * @return El nombre de la categoría.
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Obtiene la etiqueta de la categoría.
     * @return La etiqueta de la categoría.
     */
    public String getTag() {
        return tag;
    }
    
    /**
     * Obtiene el estatus de la categoría.
     * @return El estatus de la categoría.
     */
    public Integer getStatus() {
        return status;
    }
}