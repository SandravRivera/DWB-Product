package com.product.exception;

import org.springframework.dao.DataAccessException;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.context.request.ServletWebRequest;

public class DBAccessException extends RuntimeException{
	
    private static final long serialVersionUID = 1L;
    private DataAccessException exception;
	
	public DBAccessException(DataAccessException e) {
		this.exception = e;
	}


}
