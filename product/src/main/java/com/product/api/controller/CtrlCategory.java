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
    public ResponseEntity<String> create(@Valid @RequestBody DtoCategoryIn in) {
        svc.create(in); 
        String msg = "La categoría ha sido registrada"+
            "\nCategoría: " + in.getCategory()+ 
            "\nTag: "+ in.getTag();
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@Valid @RequestBody DtoCategoryIn in, 
        @PathVariable Integer id) {
        svc.update(in, id);
        String msg = "La categoría ha sido actualizada"+
            "\nCategoría: " + in.getCategory()+ 
            "\nTag: "+ in.getTag();
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<String> enable(@PathVariable Integer id) {
        svc.enable(id);
        return ResponseEntity.ok("La categoría con id "+id+" ha sido activada");
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> disable(@PathVariable Integer id) {
        svc.disable(id);
        return ResponseEntity.ok("La categoría con id "+id+" ha sido desactivada");
    }
    
}
