package com.product.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.product.api.entity.CartItem;

import jakarta.transaction.Transactional;

@Repository
public interface RepoCartItem extends JpaRepository<CartItem, Integer> {

    // Mostrar todos los artículos del carrito
    List<CartItem> findByUserId(Integer userId);

    // Determinar la existencia de un artículo en especial    
    boolean existsByCartItemIdAndUserId(Integer cartItemId, Integer userId);

    // Obtener un artículo por su ID
    Optional<CartItem> findById(Integer id);
    CartItem findByProductIdAndUserId(Integer productId, Integer userId);

    // Eliminar un solo producto
    @Transactional
    @Modifying
    void deleteByProductIdAndUserId(Integer productId, Integer userId);

    // Eliminar todo
    @Transactional
    @Modifying
    int deleteByUserId(Integer userId);
}
