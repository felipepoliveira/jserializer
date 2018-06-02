package io.felipepoliveira.jserializer.exceptions;

public class JfoParseException extends JsonParseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JfoParseException() {
		super();
	}

	public JfoParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JfoParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public JfoParseException(String message) {
		super(message);
	}

	public JfoParseException(Throwable cause) {
		super(cause);
	}
	
	

}
