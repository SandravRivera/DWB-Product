package com.product.api.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.product.api.entity.Category;

import jakarta.transaction.Transactional;

/**
 * Repositorio para la entidad Category.
 * Proporciona métodos para gestionar el ciclo de vida de las categorías 
 * mediante consultas nativas y métodos derivados de JPA.
 */
@Repository
public interface RepoCategory extends JpaRepository<Category,Integer> {

    /**
     * Recupera todas las categorías de la base de datos.
     * @return Lista de todas las categorías ordenadas alfabéticamente.
     */
    @Query(value="SELECT * FROM category ORDER BY category", nativeQuery=true)
    List<Category> findAll();

    /**
     * Filtra categorías por su estado de activación.
     * @param status El estado a filtrar.
     * @return Lista de categorías que coinciden con el estado, ordenadas por nombre.
     */
    List<Category> findByStatusOrderByCategory(@Param("status") Integer status);

    /**
     * Inserta una nueva categoría en la base de datos con estado activo por defecto.
     * @param category Nombre descriptivo de la categoría.
     * @param tag      Etiqueta identificadora.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "INSERT INTO category(category, tag, status) VALUES (:category, :tag, 1)", nativeQuery = true)
    void create(@Param("category") String category, @Param("tag") String tag);    

    /**
     * Actualiza la información básica de una categoría existente.
     * @param category_id Identificador de la categoría.
     * @param category    Nuevo nombre.
     * @param tag         Nueva etiqueta.
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE category SET category = :category, tag = :tag WHERE category_id = :category_id", nativeQuery = true)
    void update(@Param("category_id") Integer category_id, @Param("category") String category, @Param("tag") String tag);

    /**
     * Realiza un cambio de estado sobre una categoría.
     * @param category_id Identificador de la categoría.
     * @param status      Nuevo estado.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE category SET status = :status WHERE category_id = :category_id", nativeQuery = true)
    void updateStatus(@Param("category_id") Integer category_id, @Param("status") Integer status);
}