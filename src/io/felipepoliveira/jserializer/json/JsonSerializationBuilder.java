package io.felipepoliveira.jserializer.json;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
	public JsonSerializationBuilder withJfo(String jfo) throws JfoParseException, JsonParseException {
		this.jfo = serializer.parseJfo(jfo);
		
		return this;
	}
	
	public JfoObject parseJfo(String jfo) {
		return serializer.parseJfo(jfo);
	}
	
	/**
	 * Defines the JSON Filter Object used to serialize the object
	 * @param jfo
	 */
	public JsonSerializationBuilder withJfo(JfoObject jfo) {
		this.jfo = jfo;
		
		return this;
	}
	
	/**
	 * Specify the fields used to serialize the object
	 * @param fields
	 */
	public JsonSerializationBuilder withFields(String...fields) {
		this.parameters.setType(JsonFieldAccesTypes.INCLUDE);
		parameters.setFields(fields);
		
		return this;
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
	
	public JsonArray serializeArray(Object[] objects) {
		applyJfoInSerializationParameters();
		return serializer.serialize(objects, parameters);
	}
	
	public JsonArray serializeCollection(Collection<Object> objects) {
		applyJfoInSerializationParameters();
		return serializer.serialize(objects, parameters);
	}
	
	private void applyJfoInSerializationParameters() {
		if(this.jfo != null) {
			
			if(!this.parameters.hasFields()) {
				parameters.setType(jfo.getFieldFilterAsJsonFieldAccessType());
				parameters.setFields(jfo.getFilteredFields());
			}
			else if(this.parameters.getType() == JsonFieldAccesTypes.INCLUDE) {
				
				if(jfo.getFieldFilter() == JfoFieldFilters.REQUIRE) {
					removeFromParametersIfNotExistsInJfo();
				}
				else if(jfo.getFieldFilter() == JfoFieldFilters.EXCLUDE) {
					removeFromParametersIfExistsInJfo();
				}
				
			}
			else if (this.parameters.getType() == JsonFieldAccesTypes.EXCLUDE) {
				if(jfo.getFieldFilter() == JfoFieldFilters.REQUIRE) {
					parameters.setType(JsonFieldAccesTypes.INCLUDE);
					addInParametersIfExistsOnlyInJfo();
					
				}
				else if(jfo.getFieldFilter() == JfoFieldFilters.EXCLUDE) {
					parameters.getFields().addAll(jfo.getFilteredFields());
				}
			}
		}
	}
	
	private void addInParametersIfExistsOnlyInJfo() {
		Set<String> fields = new HashSet<>();
		fields.addAll(jfo.getFilteredFields());
		for (String jfoField : jfo.getFilteredFields()) {
			
			if(parameters.getFields().contains(jfoField)) {
				fields.remove(jfoField);
			}
			
		}
		
		parameters.setFields(fields);
	}
	
	private void removeFromParametersIfNotExistsInJfo() {
		Iterator<String> iterator = parameters.getFields().iterator();
		String field = null;
		while(iterator.hasNext() && (field = iterator.next()) != null) {
			if(!jfo.getFilteredFields().contains(field)) {
				iterator.remove();
			}
		}
	}
	
	private void removeFromParametersIfExistsInJfo() {
		Iterator<String> iterator = parameters.getFields().iterator();
		String field = null;
		while(iterator.hasNext() && (field = iterator.next()) != null) {
			if(jfo.getFilteredFields().contains(field)) {
				iterator.remove();
			}
		}
	}
}
