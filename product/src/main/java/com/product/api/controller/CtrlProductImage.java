package com.product.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.dto.out.DtoProductImageOut;
import com.product.api.service.SvcProductImage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/product/{id}/image")
@Tag(name = "Product Image", description = "Catálogo de las imágenes de un producto")
public class CtrlProductImage {

    @Autowired
    SvcProductImage svc;

    @GetMapping
	@Operation(summary = "Consultar imágenes", description = "Lista las imágenes de un producto registradas en el sistema")
    public ResponseEntity<List<DtoProductImageOut>> getProductImages(@PathVariable Integer id) {
        // return ResponseEntity.ok(svc.getProductImages(id));
        return svc.getProductImages(id);
    }

    @PostMapping
    @Operation(summary = "Registrar imagen", description = "Registra una nueva imagen de un producto en el sistema")
    public ResponseEntity<String> createProductImage(@Valid @RequestBody DtoProductImageIn in) {
        // return ResponseEntity.ok(svc.uploadProductImage(in));
        return svc.upload(in);
    }

    @DeleteMapping("/{product_image_id}")
    @Operation(summary = "Eliminar imagen", description = "Elimina una imagen de un producto específica")
    public ResponseEntity<String> deleteProductImage(@PathVariable Integer product_image_id) {
        // return ResponseEntity.ok(svc.deleteProductImage(id));
        return svc.deleteProductImage(product_image_id);
    }
    
    
}
