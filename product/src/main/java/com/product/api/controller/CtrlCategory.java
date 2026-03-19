package com.product.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.service.SvcCategory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
public class CtrlCategory {

    @Autowired
    SvcCategory svc;
    
    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(svc.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Category>> findActive() {
        return ResponseEntity.ok(svc.findActive());
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody DtoCategoryIn in) {
        svc.create(in); 
        System.out.println("La categoría ha sido registrada");
        System.out.println("Categoría: "+ in.getCategory());
        System.out.println("Tag: "+ in.getTag());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@Valid @RequestBody DtoCategoryIn in, 
        @PathVariable Integer id) {
        svc.update(in, id);
        System.out.println("La categoría ha sido actualizada");
        System.out.println("Categoría: "+ in.getCategory());
        System.out.println("Tag: "+ in.getTag());
        return ResponseEntity.ok().build();

    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<Void> enable(@PathVariable Integer id) {
        svc.enable(id);
        System.out.println("La categoría ha sido activada");
        System.out.println("ID: "+ id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disable(@PathVariable Integer id) {
        svc.disable(id);
        System.out.println("La categoría ha sido desactivada");
        System.out.println("ID: "+ id);
        return ResponseEntity.ok().build();
    }
    
}
