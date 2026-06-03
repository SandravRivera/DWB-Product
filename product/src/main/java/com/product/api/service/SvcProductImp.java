package com.product.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.product.api.dto.in.DtoProductIn;
import com.product.api.dto.out.DtoProductListOut;
import com.product.api.dto.out.DtoProductOut;
import com.product.api.entity.Category;
import com.product.api.entity.Product;
import com.product.api.entity.ProductImage;
import com.product.api.repository.RepoCategory;
import com.product.api.repository.RepoProduct;
import com.product.api.repository.RepoProductImage;
import com.product.common.mapper.MapperProduct;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;

@Service
public class SvcProductImp implements SvcProduct{
	
	@Autowired
	RepoProduct repo;

	@Autowired
    RepoProductImage repoProductImage;
	
	@Autowired
	MapperProduct mapper;

	@Autowired
	RepoCategory repoCategory;

	@Value("${app.upload.dir}")
	private String uploadDir; 

	@Value("${app.upload.images}")
	private String uploadImages;

	@Override
	public ResponseEntity<List<DtoProductListOut>> getProducts() {
		try {
			List<Product> products = repo.findAll();
			return new ResponseEntity<>(mapper.fromProductList(products), HttpStatus.OK);
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<DtoProductOut> getProduct(Integer id) {
		try {
			// Verificar que el producto existe
			validateProductId(id);
		
			// Obtener la entidad Product real directamente del repositorio modificado
			Product product = repo.getProduct(id);
			if(product == null)
				throw new ApiException(HttpStatus.NOT_FOUND, "El id del producto no existe");
			
			// Mapear Product a DtoProductOut
			DtoProductOut productDto = new DtoProductOut();
			productDto.setProduct_id(product.getProduct_id());
			productDto.setGtin(product.getGtin());
			productDto.setProduct(product.getProduct());
			productDto.setDescription(product.getDescription());
			productDto.setPrice(product.getPrice());
			productDto.setStock(product.getStock());
			
			// Obtener el nombre de la categoría (optional) y registrarlo
			Category category = repoCategory.findById(product.getCategory_id())
            	.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "El id de categoría no existe"));
        	productDto.setCategory(category.getCategory());
			
			// Leer y adjuntar los archivos de imágenes en Base64
			List<String> imagesBase64 = readProductImagesFiles(id);
			productDto.setImages(imagesBase64);
			
			// Devolver DTO
			return new ResponseEntity<>(productDto, HttpStatus.OK);

		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<String> createProduct(DtoProductIn in) {
		try {
			Product product = mapper.fromDto(in);
			repo.save(product);
			return new ResponseEntity<>("El producto ha sido registrado", HttpStatus.CREATED);
		}catch (DataAccessException e) {
			if (e.getLocalizedMessage().contains("ux_product_gtin"))
				throw new ApiException(HttpStatus.CONFLICT, "El gtin del producto ya está registrado");
			if (e.getLocalizedMessage().contains("ux_product_product"))
				throw new ApiException(HttpStatus.CONFLICT, "El nombre del producto ya está registrado");
			if (e.getLocalizedMessage().contains("fk_product_category"))
				throw new ApiException(HttpStatus.NOT_FOUND, "El id de categoría no existe");

			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<String> updateProduct(Integer id, DtoProductIn in) {
		try {
			validateProductId(id);
			Product product = mapper.fromDto(id, in);
			repo.save(product);
			return new ResponseEntity<>("El producto ha sido actualizado", HttpStatus.OK);
		}catch (DataAccessException e) {
			if (e.getLocalizedMessage().contains("ux_product_gtin"))
				throw new ApiException(HttpStatus.CONFLICT, "El gtin del producto ya está registrado");
			if (e.getLocalizedMessage().contains("ux_product_product"))
				throw new ApiException(HttpStatus.CONFLICT, "El nombre del producto ya está registrado");
			if (e.getLocalizedMessage().contains("fk_product_category"))
				throw new ApiException(HttpStatus.NOT_FOUND, "El id de categoría no existe");

			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<String> enableProduct(Integer id) {
		try {
			validateProductId(id);
			Product product = repo.findById(id).get();
			product.setStatus(1);
			repo.save(product);
			return new ResponseEntity<>("El producto ha sido activado", HttpStatus.OK);
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	@Override
	public ResponseEntity<String> disableProduct(Integer id) {
		try {
			validateProductId(id);
			Product product = repo.findById(id).get();
			product.setStatus(0);
			repo.save(product);
			return new ResponseEntity<>("El producto ha sido desactivado", HttpStatus.OK);
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}
	
	private void validateProductId(Integer id) {
		try {
			if(repo.findById(id).isEmpty()) {
				throw new ApiException(HttpStatus.NOT_FOUND, "El id del producto no existe");
			}
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	private List<String> readProductImagesFiles(Integer product_id) {
		try {
			// Obtener imágenes
			List<ProductImage> productImages = repoProductImage.findByProductId(product_id);
			// No hay imágenes, arreglo vacío
			if(productImages == null || productImages.size() == 0)
				return new ArrayList<>();
			// Almacenar imágenes codificadas en Base64
			List<String> imagesUrl = new ArrayList<>();
			for(ProductImage productImage : productImages) {
				String imageUrl = productImage.getImage();
				// Si la URL comienza con "/" la eliminamos para obtener la relativa
                if (imageUrl.startsWith("/")) 
                        imageUrl = imageUrl.substring(1);
                // Construir el Path
				Path imagePath = Paths.get(uploadDir, uploadImages, imageUrl);
                // Verifica que el archivo exista
                if (!Files.exists(imagePath)) 
					continue; // No existe, no se guarda
                // Leer los bytes de la imagen y codificarlos a Base64
                byte[] imageBytes = Files.readAllBytes(imagePath);
                imageUrl = Base64.getEncoder().encodeToString(imageBytes);
				imagesUrl.add(imageUrl);
			}
			// Devolver arreglo con las imagenes codificadas
            return imagesUrl;
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        } catch (IOException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al leer el archivo");
        }
	}
}
