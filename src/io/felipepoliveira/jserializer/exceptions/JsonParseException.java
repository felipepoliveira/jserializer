package io.felipepoliveira.jserializer.exceptions;

import io.felipepoliveira.jserializer.json.JsonObject;

/**
 * Exception used while trying to parse String to {@link JsonObject}
 * @author Felipe Oliveira
 *
 */
public class JsonParseException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonParseException() {
		super();
	}

	public JsonParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JsonParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonParseException(String message) {
		super(message);
	}

	public JsonParseException(Throwable cause) {
		super(cause);
	}
	
	

}
