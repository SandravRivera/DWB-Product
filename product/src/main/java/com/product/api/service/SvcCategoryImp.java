package com.product.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;

import java.util.List;


@Service
public class SvcCategoryImp implements SvcCategory {

    @Autowired
    RepoCategory repo;

    @Override
    public List<Category> findAll() {
        try {
            return repo.findAll();
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }
    
    @Override
    public List<Category> findActive() {
        try {
            return repo.findByStatusOrderByCategory(1);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }
    
    @Override
    public void create(DtoCategoryIn in) {
        try {
            repo.create(in.getCategory(), in.getTag());
        } catch (DataAccessException e) {
            manageDAE(e);
        }
    }
    
    @Override
    public void update(DtoCategoryIn in, Integer id) {
        try {
            repo.update(id, in.getCategory(), in.getTag());
        } catch (DataAccessException e) {
            manageDAE(e);
        }
    }
    
    @Override
    public void enable(Integer id) {
        updateStatus(id, 1);
    }
    
    @Override
    public void disable(Integer id) {
        updateStatus(id, 0);
    }

    // private updateStatus()
    private void updateStatus(Integer id, Integer status) {
        try {
            if(repo.findById(id).isEmpty())
                throw new ApiException(HttpStatus.NOT_FOUND, "El id de la categoría no existe");
            repo.updateStatus(id, status);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }

    private void manageDAE(DataAccessException e) {
        String msg = e.getMostSpecificCause().getMessage();
        if(msg.contains("category.category"))
            throw new ApiException(HttpStatus.CONFLICT, "El nombre de la categoría ya está registrado");
        if(msg.contains("category.tag"))
            throw new ApiException(HttpStatus.CONFLICT, "El tag de la categoría ya está registrado");
    }
}
