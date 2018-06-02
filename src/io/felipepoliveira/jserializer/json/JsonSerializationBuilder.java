package io.felipepoliveira.jserializer.json;

import io.felipepoliveira.jserializer.SerializationBuilder;
import io.felipepoliveira.jserializer.exceptions.JfoParseException;
import io.felipepoliveira.jserializer.exceptions.JsonParseException;

/**
 * The Builder class to create an easy instance of the {@link JsonSerializer} object
 * @author Felipe Oliveira
 */
public class JsonSerializationBuilder implements SerializationBuilder {
	
	/**
	 * The builded {@link JsonSerializer} from this builder instance
	 */
	private JsonSerializer serializer;
	
	/**
	 * Store the serialization parameters
	 */
	private JsonSerializationParameters parameters;
	
	/**
	 * Store data about JSON Filter Object
	 */
	private JfoObject jfo;
	
	public JsonSerializationBuilder() {
		serializer = new JsonSerializer();
		parameters = new JsonSerializationParameters();
	}
	
	/**
	 * Defines the JSON Filter Object used to serialize the object
	 * @param jfo - The string representation of the JFO object
	 * @throws JfoParseException - If the extracted object is a invalid JFO 
	 * @throws JsonParseException - If the given JSON from jfo parameters is a invalid JSON
	 */
	public void withJfo(String jfo) throws JfoParseException, JsonParseException {
		this.jfo = serializer.parseJfo(jfo);
	}
	
	public JfoObject parseJfo(String jfo) {
		return serializer.parseJfo(jfo);
	}
	
	/**
	 * Defines the JSON Filter Object used to serialize the object
	 * @param jfo
	 */
	public void withJfo(JfoObject jfo) {
		this.jfo = jfo;
	}
	
	/**
	 * Specify the fields used to serialize the object
	 * @param fields
	 */
	public void withFields(String...fields) {
		parameters.setFields(fields);
	}
	
	/**
	 * Specify the fields used to serialize the object. It can use the {@link JsonFieldAccesTypes}
	 * to define if the serialization with include or exclude the fields
	 * @param access
	 * @param fields
	 */
	public JsonSerializationBuilder withFields(JsonFieldAccesTypes access, String... fields) {
		parameters.setType(access);
		parameters.setFields(fields);
		
		return this;
	}
	
	/**
	 * Call the internal object of this class: {@link JsonSerializer}.parse(String) method. This method
	 * parse an given string to an {@link JsonStructure}
	 * @param json - The json string
	 * @return the generic {@link JsonStructure} representation
	 * @throws JsonParseException
	 */
	public JsonStructure parse(String json) throws JsonParseException {
		return serializer.parse(json);
	}
	
	public JsonObject serialize(Object object) {
		applyJfoInSerializationParameters();
		return serializer.serialize(object, parameters);
	}
	
	private void applyJfoInSerializationParameters() {
		if(this.jfo != null) {
			
		}
	}
}
