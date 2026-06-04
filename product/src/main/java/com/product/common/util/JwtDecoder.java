package com.product.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.product.exception.ApiException;

@Component
public class JwtDecoder {
	
	public boolean isAdmin() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication != null && authentication.isAuthenticated()) {
	            return authentication.getAuthorities()
	                .stream()
	                .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
	        }

	        return false;
		}catch(Exception e) {
			System.out.println("El usuario no es administrador");
			return false;
		}
	}
	
	public Integer getUserId() {
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        Object details = authentication.getDetails();

	        if (details instanceof Number number) {
	            return number.intValue();
	        }

	        if (details instanceof String id) {
	            return Integer.valueOf(id);
	        }

	        throw new ApiException(HttpStatus.PRECONDITION_FAILED, "El usuario es inválido");

	    } catch (Exception e) {
	        throw new ApiException(HttpStatus.PRECONDITION_FAILED, "El usuario es inválido");
	    }
	}
}