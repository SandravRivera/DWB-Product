package com.product.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.dto.out.DtoProductImageOut;
import com.product.api.entity.ProductImage;
import com.product.api.repository.RepoProduct;
import com.product.api.repository.RepoProductImage;
import com.product.common.mapper.MapperProduct;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;

@Service
public class SvcProductImageImp implements SvcProductImage {
	
	@Autowired
	RepoProduct repoProduct;

	@Autowired
    RepoProductImage repoImage;
	
	@Autowired
	MapperProduct mapper;

	@Value("${app.upload.dir}")
    private String uploadDir;
	
	@Value("${app.upload.images}")
	private String uploadImages;
	
	@Override
	public ResponseEntity<List<DtoProductImageOut>> getProductImages(Integer productId){
		try {
			// Verificar que el producto existe
			validateProductId(productId);

			// Obtener las imágenes de la base de datos
			List<ProductImage> images = repoImage.findByProductId(productId);
			
			// Mapear ProductImage a DtoProductImageOut
			List<DtoProductImageOut> dtos = new ArrayList<>();
			for (ProductImage img : images) {
				DtoProductImageOut dto = new DtoProductImageOut();
				dto.setProductImageId(img.getProductImageId());
				dto.setProductId(img.getProductId());
				dto.setImage(img.getImage());
				dto.setStatus(img.getStatus());
				dtos.add(dto);
			}
			
			// Devolver DTOs
			return new ResponseEntity<>(dtos, HttpStatus.OK);

		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<String> upload(DtoProductImageIn in) {
		try {
			// PASO 0: Validar prefijo del String de Base64
			// Eliminar el prefijo "data:image/png;base64," si existe
			if (in.getImage().startsWith("data:image")) {
				int commaIndex = in.getImage().indexOf(",");
				if (commaIndex != -1)
					in.setImage(in.getImage().substring(commaIndex + 1));
			}

			// PASO 1: Convertir el Base64 en un File
			// Decodifica la cadena Base64 a bytes
			byte[] imageBytes = Base64.getDecoder().decode(in.getImage());
			// Genera un nombre único para la imagen (se asume extensión PNG)
			String fileName = UUID.randomUUID().toString() + ".png";
			// Construye la ruta completa donde se guardará la imagen
			Path imagePath = Paths.get(uploadDir, uploadImages, "product", fileName);
			
			// PASO 2: Guardar el File en el sistema de archivos
			// Asegurarse de que el directorio exista
			Files.createDirectories(imagePath.getParent());
			// Escribir el archivo en el sistema de archivos
			Files.write(imagePath, imageBytes);
			
			// PASO 3: Guardar la ruta en la base de datos
			// Crear la entidad ProductImage y guardar la URL en la base de datos
			ProductImage productImage = new ProductImage();
			productImage.setProductId(in.getProductId());
			productImage.setImage("/product/" + fileName);
			productImage.setStatus(1); 
			// Guardar la ruta de la imagen
			repoImage.save(productImage);

			return new ResponseEntity<>("La imagen ha sido registrada", HttpStatus.CREATED);

		} catch (DataAccessException e) {
			throw new DBAccessException(e);
		} catch (IOException e) {
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo");
		}

	}

	@Override
	public ResponseEntity<String> deleteProductImage(Integer id) {	
		try {
			validateProductImageId(id);
			ProductImage productImage = repoImage.findById(id).get();
			String relativePath = productImage.getImage();
			String fileName = relativePath.substring(relativePath.lastIndexOf("/") + 1);
			Path imagePath = Paths.get(uploadDir, uploadImages, "product", fileName);
			if (Files.exists(imagePath)) 
				Files.delete(imagePath);
			repoImage.delete(productImage);
			return new ResponseEntity<>("La imagen ha sido eliminada", HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }catch (IOException e) {
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el archivo");
		}
	}

    // AUXILIARES

	private void validateProductId(Integer id) {
		try {
			if(repoProduct.findById(id).isEmpty()) {
				throw new ApiException(HttpStatus.NOT_FOUND, "El id del producto no existe");
			}
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}
    
	private void validateProductImageId(Integer id) {
		try {
            if (!repoImage.existsById(id)) {
                throw new ApiException(HttpStatus.NOT_FOUND, "El id de la imagen no existe");
            }
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}
}
