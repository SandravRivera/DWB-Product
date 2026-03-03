package com.product.api.controller;

import org.springframework.web.bind.annotation.RestController;
import com.product.api.entity.Category;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class CtrlCategory {
    @GetMapping("/category")
    public Category[] showCategories() {
        Category c1 = new Category(1, "Electrónica", "TECH", 1);
        Category c2 = new Category(2, "Ropa", "FASHION", 1);
        Category c3 = new Category(3, "Hogar", "HOME", 1);

        return new Category[]{c1, c2, c3};
    }
    
}
