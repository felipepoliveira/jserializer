package io.felipepoliveira.jserializer.json;

/**
 * An interface that represents a Json structure
 * @author Felipe Oliveira
 *
 */
public interface JsonStructure {
	
	/**
	 * @return Flag indicating if the instance of this interface is of a {@link JsonArray}
	 */
	public default boolean isJsonArray() {
		return (this instanceof JsonArray);
	}
	
	/**
	 * @return Cast the instance of this object to {@link JsonArray} object. For safety use of this method
	 * is recommended to use the JsonData.isJsonArray() method to check the instance of this object
	 */
	public default JsonArray asJsonArray() {
		return (JsonArray) this;
	}
	
	/**
	 * @return Cast the instance of this object to {@link JsonObject} object. For safety use of this method
	 * is recommended to use the JsonData.isJsonObject() method to check the instance of this object
	 */
	public default JsonObject asJsonObject() {
		return (JsonObject) this;
	}
	
	/**
	 * 
	 * @return flag indicating if the instance of this interface is of a {@link JsonObject}
	 */
	public default boolean isJsonObject() {
		return (this instanceof JsonObject);
	}
}
