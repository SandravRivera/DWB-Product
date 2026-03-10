package com.product;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class CtrlProduct {
    @GetMapping("/category")
    public Category[] showCategories() {
        Category c1 = new Category(1, "Electrónica", "TECH", 1);
        Category c2 = new Category(2, "Ropa", "FASHION", 1);
        Category c3 = new Category(3, "Hogar", "HOME", 1);

        return new Category[]{c1, c2, c3};
    }
    
}
