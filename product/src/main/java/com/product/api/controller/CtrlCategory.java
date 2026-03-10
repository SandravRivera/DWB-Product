package com.product.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.product.api.entity.Category;
import com.product.api.service.SvcCategory;

@RestController
@RequestMapping("/category")
public class CtrlCategory {

    @Autowired
    SvcCategory svc;

    @GetMapping
    public List<Category> getCategories() {
        return svc.getCategories();
    }
    
}
