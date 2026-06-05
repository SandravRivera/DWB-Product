package com.product.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.product.api.entity.Product;
import java.util.Optional;



@Repository
public interface RepoProduct extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT * FROM product WHERE product_id = :product_id", nativeQuery = true)
    Product getProduct(@Param("product_id") Integer product_id);

    Optional<Product> findByGtin(String gtin);

}
