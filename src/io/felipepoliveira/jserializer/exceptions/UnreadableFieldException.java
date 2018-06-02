package io.felipepoliveira.jserializer.exceptions;


public class UnreadableFieldException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnreadableFieldException() {
		super();
	}

	public UnreadableFieldException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnreadableFieldException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnreadableFieldException(String message) {
		super(message);
	}

	public UnreadableFieldException(Throwable cause) {
		super(cause);
	}
	
	

}
