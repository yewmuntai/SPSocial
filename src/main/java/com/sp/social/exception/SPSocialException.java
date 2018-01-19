package com.sp.social.exception;

public class SPSocialException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public SPSocialException(String errorMsg) {
		super(errorMsg);
	}
}
