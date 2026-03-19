package com.product.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.service.SvcCategory;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;

import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de categorías de productos.
 * Proporciona endpoints para operaciones CRUD, filtrado por estado
 * y activación/desactivación lógica de registros.
 * Maneja excepciones globales a través de {@code RestExceptionHandler} 
 * para devolver respuestas estructuradas en caso de error.
 */
@RestController
@RequestMapping("/category")
public class CtrlCategory {

    @Autowired
    SvcCategory svc;
    
    /**
     * Recupera el listado completo de categorías registradas.
     * @return {@code ResponseEntity} con la lista de todas las categorías.
     * Status: 200 OK.
     * @throws DBAccessException si ocurre un error inesperado en la base de datos.
     */
    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(svc.findAll());
    }

    /**
     * Recupera únicamente las categorías que se encuentran en estado activo (status = 1).
     * @return {@code ResponseEntity} con la lista de categorías activas.
     * Status: 200 OK.
     * @throws DBAccessException si ocurre un error de acceso a datos.
     */
    @GetMapping("/active")
    public ResponseEntity<List<Category>> findActive() {
        return ResponseEntity.ok(svc.findActive());
    }

    /**
     * Registra una nueva categoría en el sistema.
     * @param in DTO con la información de la categoría. Valida restricciones de no nulidad.
     * @return {@code ResponseEntity} con mensaje de éxito.
     * Status: 200 OK.
     * @throws ApiException 409 CONFLICT si el nombre de la categoría o el tag ya existen.
     * @throws MethodArgumentNotValidException 400 BAD REQUEST si los datos de entrada son inválidos.
     */
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody DtoCategoryIn in) {
        svc.create(in); 
        String msg = "La categoría ha sido registrada"+
            "\nCategoría: " + in.getCategory()+ 
            "\nTag: "+ in.getTag();
        return ResponseEntity.ok(msg);
    }

    /**
     * Actualiza los datos de una categoría existente.
     * @param in DTO con los nuevos datos (category y tag).
     * @param id Identificador único de la categoría a modificar.
     * @return {@code ResponseEntity} con mensaje de actualización exitosa.
     * Status: 200 OK.
     * @throws ApiException 404 NOT FOUND si el ID no existe.
     * @throws ApiException 409 CONFLICT si los nuevos datos violan restricciones de unicidad.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@Valid @RequestBody DtoCategoryIn in, 
        @PathVariable Integer id) {
        svc.update(in, id);
        String msg = "La categoría ha sido actualizada"+
            "\nCategoría: " + in.getCategory()+ 
            "\nTag: "+ in.getTag();
        return ResponseEntity.ok(msg);
    }

    /**
     * Activa una categoría específica.
     * @param id Identificador único de la categoría a habilitar.
     * @return {@code ResponseEntity} confirmando la activación.
     * Status: 200 OK.
     * @throws ApiException 404 NOT FOUND si el ID no existe.
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<String> enable(@PathVariable Integer id) {
        svc.enable(id);
        return ResponseEntity.ok("La categoría con id "+id+" ha sido activada");
    }

    /**
     * Desactiva una categoría específica (Borrado lógico).
     * @param id Identificador único de la categoría a deshabilitar.
     * @return {@code ResponseEntity} confirmando la desactivación.
     * Status: 200 OK.
     * @throws ApiException 404 NOT FOUND si el ID no existe.
     */
    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> disable(@PathVariable Integer id) {
        svc.disable(id);
        return ResponseEntity.ok("La categoría con id "+id+" ha sido desactivada");
    }
    
}