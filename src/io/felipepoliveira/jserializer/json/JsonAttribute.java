package io.felipepoliveira.jserializer.json;

/**
 * Represents an JSON Object attribute (used in {@link JsonObject} class). It stores the name and the {@link JsonValue}
 * of an attribute of a JSON object
 * @author Felipe Oliveira
 *
 */
public class JsonAttribute implements JsonData {
	
	/**
	 * The name of the json attribute
	 */
	private String name;
	
	/**
	 * The value of the attribute
	 * @see JsonValue
	 */
	private JsonValue value;
	
	/**
	 * Create an instance of JsonAttribute object
	 */
	public JsonAttribute() {}
	
	/**
	 * Create an instance of JsonAttribute object
	 * @param name - The name of the json attribute
	 * @param value - The value of the json attribute
	 */
	public JsonAttribute(String name, JsonValue value) {
		setName(name);
		setValue(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JsonValue getValue() {
		return value;
	}

	public void setValue(JsonValue value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "\"" + this.getName() + "\"" + ":" + getValue().toString();
	}
}
