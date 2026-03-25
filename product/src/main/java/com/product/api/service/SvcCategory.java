package com.product.api.service;

import java.util.List;

import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;

/**
 * Interfaz de servicio para la gestión de categorías.
 * Define las operaciones de negocio permitidas para el catálogo de productos.
 */
public interface SvcCategory {

    /**
     * Obtiene el listado de todas las categorías, sin importar su estado.
     * @return Lista con todas las entidades {@link Category}.
     */
    public List<Category> findAll();

    /**
     * Recupera únicamente las categorías marcadas como activas.
     * @return Lista de categorías activas.
     */
    public List<Category> findActive();

    /**
     * Procesa la creación de una nueva categoría.
     * @param in Objeto DTO con los datos de entrada validados.
     */
    public void create(DtoCategoryIn in);

    /**
     * Actualiza los datos de una categoría existente.
     * @param in Objeto DTO con la nueva información.
     * @param id Identificador único de la categoría a modificar.
     */
    public void update(DtoCategoryIn in, Integer id);

    /**
     * Habilita una categoría en el sistema.
     * @param id Identificador de la categoría a activar.
     */
    public void enable(Integer id);

    /**
     * Deshabilita una categoría (borrado lógico).
     * @param id Identificador de la categoría a desactivar.
     */
    public void disable(Integer id);
}