package com.product.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;

import java.util.List;

/**
 * Implementación de la interfaz de servicio para la gestión de categorías.
 * Contiene la lógica de negocio y el manejo de excepciones de persistencia.
 */
@Service
public class SvcCategoryImp implements SvcCategory {

    @Autowired
    RepoCategory repo;

    /**
     * Recupera todas las categorías registradas.
     * @return Lista de categorías.
     * @throws DBAccessException si ocurre un error inesperado en la base de datos.
     */
    @Override
    public List<Category> findAll() {
        try {
            return repo.findAll();
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }
    
    /**
     * Recupera las categorías con estatus activo (1).
     * @return Lista de categorías activas.
     * @throws DBAccessException si ocurre un error de acceso a datos.
     */
    @Override
    public List<Category> findActive() {
        try {
            return repo.findByStatusOrderByCategory(1);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }
    
    /**
     * Crea una nueva categoría capturando errores de duplicidad.
     * @param in DTO con la información de la categoría.
     */
    @Override
    public void create(DtoCategoryIn in) {
        try {
            repo.create(in.getCategory(), in.getTag());
        } catch (DataAccessException e) {
            manageDAE(e);
        }
    }
    
    /**
     * Actualiza una categoría existente capturando errores de duplicidad.
     * @param in DTO con los nuevos datos.
     * @param id Identificador de la categoría a actualizar.
     */
    @Override
    public void update(DtoCategoryIn in, Integer id) {
        try {
            repo.update(id, in.getCategory(), in.getTag());
        } catch (DataAccessException e) {
            manageDAE(e);
        }
    }
    
    /**
     * Habilita una categoría en el sistema.
     * @param id Identificador de la categoría a activar.
     */
    @Override
    public void enable(Integer id) {
        updateStatus(id, 1);
    }
    
    /**
     * Deshabilita una categoría (borrado lógico).
     * @param id Identificador de la categoría a desactivar.
     */
    @Override
    public void disable(Integer id) {
        updateStatus(id, 0);
    }

    /**
     * Método privado para realizar la actualización del estatus.
     * @param id Identificador de la categoría.
     * @param status Valor del nuevo estatus (0 o 1).
     * @throws ApiException 404 si el ID no existe.
     * @throws DBAccessException si ocurre un error de acceso a datos.
     */
    private void updateStatus(Integer id, Integer status) {
        try {
            if(repo.findById(id).isEmpty())
                throw new ApiException(HttpStatus.NOT_FOUND, "El id de la categoría no existe");
            repo.updateStatus(id, status);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }

    /**
     * Analiza las excepciones de base de datos para identificar violaciones de valores únicos.
     * @param e Excepción de acceso a datos capturada.
     * @throws ApiException 409 CONFLICT si se detecta un duplicado en categoría o tag.
     */
    private void manageDAE(DataAccessException e) {
        String msg = e.getMostSpecificCause().getMessage();
        if(msg.contains("category.category"))
            throw new ApiException(HttpStatus.CONFLICT, "El nombre de la categoría ya está registrado");
        if(msg.contains("category.tag"))
            throw new ApiException(HttpStatus.CONFLICT, "El tag de la categoría ya está registrado");
    }
}