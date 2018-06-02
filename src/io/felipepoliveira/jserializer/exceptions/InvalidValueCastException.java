package io.felipepoliveira.jserializer.exceptions;

public class InvalidValueCastException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidValueCastException() {
		super();
	}

	public InvalidValueCastException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidValueCastException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidValueCastException(String message) {
		super(message);
	}

	public InvalidValueCastException(Throwable cause) {
		super(cause);
	}
	

}
