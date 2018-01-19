package com.sp.social.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sp.social.data.ResponseData;
import com.sp.social.exception.SPSocialException;

import javassist.NotFoundException;

@ControllerAdvice
public class ErrorHandler {
	@ExceptionHandler(SPSocialException.class)
	public ResponseEntity<ResponseData> spException(Exception e) {
		System.out.println("sp social exception");

		return new ResponseEntity<>(new ResponseData(false, e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseData> nfException(Exception e) {
		return new ResponseEntity<>(new ResponseData(false, e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseData> exception(Exception e) {
		return new ResponseEntity<>(new ResponseData(false, e.getMessage()), HttpStatus.BAD_REQUEST);
	}
}
