package io.felipepoliveira.jserializer;

/**
 * Class that store global configuration for serialization
 * @author Felipe Oliveira
 *
 */
public class SerializationConfiguration {
	
	private boolean ignoringCycleSerializationField = true;
	
	private boolean consoleWarningLogEnabled = false;
	
	/**
	 * Enable secure serialization mode. This mode defines default configuration of serialization
	 * that prevents runtime execution errors. The definitions are:<br/>
	 * <ul>
	 * 	<li>ignoringCycleSerializationField: Se to <b>true</b>. This mode ignore circular fields</li>
	 * </ul>
	 */
	public void setToSecureSerializationMode() {
		ignoringCycleSerializationField = true;
	}
	
	/**
	 * Enable console serialization warnings. This method enable:
	 * <ul>
	 * 	<li>consoleWarningLogEnabled: set to <b>true</b>.</li>
	 * </ul>
	 */
	public void enableDebugMode() {
		this.consoleWarningLogEnabled = true;
	}

	/**
	 * Flag indicating if console warnings is enabled while using JSerialization methods
	 * @return
	 */
	public boolean isConsoleWarningLogEnabled() {
		return consoleWarningLogEnabled;
	}

	/**
	 * Define if console warnings is enables while using JSerialization methods
	 * @param consoleWarningLogEnabled
	 */
	public void setConsoleWarningLogEnabled(boolean consoleWarningLogEnabled) {
		this.consoleWarningLogEnabled = consoleWarningLogEnabled;
	}

	/**
	 * @return bool - Flag indicating if in serialization, when an cycle is verified, it will
	 * automatically ignore the field
	 */
	public boolean isIgnoringCycleSerializationField() {
		return ignoringCycleSerializationField;
	}

	/**
	 * Define if the serialization handlers will automatically ignore verified cycle fields
	 * @param ignoringCycleSerializationField
	 */
	public void setIgnoringCycleSerializationField(boolean ignoringCycleSerializationField) {
		this.ignoringCycleSerializationField = ignoringCycleSerializationField;
	}

}
