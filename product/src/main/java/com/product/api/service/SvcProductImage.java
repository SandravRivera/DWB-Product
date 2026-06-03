package com.product.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.dto.out.DtoProductImageOut;
import com.product.api.entity.ProductImage;

public interface SvcProductImage {
    public ResponseEntity<List<DtoProductImageOut>> getProductImages(Integer productID);
    // public ResponseEntity<List<ProductImage>> getProductImages(Integer productID);
    public ResponseEntity<String> upload(DtoProductImageIn in);
    public ResponseEntity<String> deleteProductImage(Integer productImageID);

}
