package com.ecommerce.orderservice.cutomexception;
import org.springframework.http.HttpStatus;

import com.ecommerce.common.exception.BaseException;

public class OrderException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderException(String code, String message) {
		super(code, message, HttpStatus.BAD_REQUEST);
		
	}

}
