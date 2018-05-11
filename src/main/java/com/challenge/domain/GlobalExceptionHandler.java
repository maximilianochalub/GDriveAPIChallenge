package com.challenge.domain;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(basePackages = "com.challenge.controllers") // pasamos package a ser controlados por excepciones
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(RuntimeException.class)
	 public ResponseEntity<String> RuntimeExceptionHandler(RuntimeException e) {
	  return new ResponseEntity<String>("holaaaaa", HttpStatus.BAD_REQUEST);
	 }
	
	
	
	
	// creamos controlador para excepciones asociadas al error http 400 bad request
	@ExceptionHandler(value = {IllegalArgumentException.class, 
    						   IllegalStateException.class, 
    						   NumberFormatException.class})
    public ResponseEntity<Object> handleBadParamsErr(RuntimeException exception, WebRequest request) {
		
		
		return handleExceptionInternal(exception, 
        							   "Los parámetros ingresados son incorrectos",
        							   new HttpHeaders(), 
        							   HttpStatus.BAD_REQUEST, 
        							   request);

    }
    
	// creamos controlador para excepciones asociadas al error http 500 internal 
    @ExceptionHandler(value = {NullPointerException.class, 
    						   RuntimeException.class, 
    						   IOException.class})
    public ResponseEntity<Object> handleInternalServerErr(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(exception, 
        							   "Error interno en el servidor", 
        							   new HttpHeaders(), 
        							   HttpStatus.INTERNAL_SERVER_ERROR, 
        							   request);
    }
  
    // creamos controlador para excepcion de autorizacion http 403 forbidden
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> handleForbiddenErr(Exception exception, WebRequest request) {
    	return new ResponseEntity<Object>("Acceso denegado. Pruebe nuevamente con la elección de la operación deseada", 
            								  new HttpHeaders(), 
            								  HttpStatus.FORBIDDEN);
    }

}