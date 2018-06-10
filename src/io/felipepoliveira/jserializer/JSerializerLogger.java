package io.felipepoliveira.jserializer;

/**
 * JSerialization library log class.
 * @author Felipe Oliveira
 *
 */
public class JSerializerLogger {
	
	/**
	 * Send an warning into application console just when JSerialization.configuration().isConsoleWarningLogEnable is set to true
	 * @param msg - The message to be displayed
	 */
	public static void warning(String msg) {
		if(JSerializer.configuration().isConsoleWarningLogEnabled()) {
			System.err.println("[JSerializer] " + msg);
		}
	}

}
