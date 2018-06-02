package io.felipepoliveira.jserializer.exceptions;

public class InvalidCursorPositionException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCursorPositionException() {
		super();
	}

	public InvalidCursorPositionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidCursorPositionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCursorPositionException(String message) {
		super(message);
	}

	public InvalidCursorPositionException(Throwable cause) {
		super(cause);
	}
	
	

}
