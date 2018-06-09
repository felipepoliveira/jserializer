package io.felipepoliveira.jserializer;

/**
 * Class that store global configuration for serialization
 * @author Felipe Oliveira
 *
 */
public class SerializationConfiguration {
	
	private boolean ignoringCycleSerializationField = true;

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
