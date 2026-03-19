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
            if(e.getLocalizedMessage().contains("ux_category"))
                throw new ApiException(HttpStatus.CONFLICT, "El nombre de la categoría ya está registrado");
            if(e.getLocalizedMessage().contains("ux_tag"))
                throw new ApiException(HttpStatus.CONFLICT, "El tag de la categoría ya está registrado");
        }
    }
    
    @Override
    public void update(DtoCategoryIn in, Integer id) {
        try {
            repo.update(id, in.getCategory(), in.getTag());
        } catch (DataAccessException e) {
            validateId(id);
            if(e.getLocalizedMessage().contains("ux_category"))
                throw new ApiException(HttpStatus.CONFLICT, "El nombre de la categoría ya está registrado");
            if(e.getLocalizedMessage().contains("ux_tag"))
                throw new ApiException(HttpStatus.CONFLICT, "El tag de la categoría ya está registrado");
            throw new DBAccessException(e);
        }
    }
    
    @Override
    public void enable(Integer id) {
        try {
            repo.updateStatus(id, 1);
        } catch (DataAccessException e) {
            validateId(id);
            throw new DBAccessException(e);
        }
    }
    
    @Override
    public void disable(Integer id) {
        try {
            repo.updateStatus(id, 0);
        } catch (DataAccessException e) {
            validateId(id);
            throw new DBAccessException(e);
        }
    }

    // private validate()
    private void validateId(Integer id) {
        if(repo.findById(id).isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND, "El id de la categoría no existe");
    }
}
