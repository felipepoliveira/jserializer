package io.felipepoliveira.jserializer.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An JSON object representations. Objects of this class represents an JSON string with all its attributes
 * @author Felipe Oliveira
 */
public class JsonObject implements JsonStructure, JsonData {
	
	/**
	 * The attributers of this object
	 */
	private Map<String, JsonAttribute> attributes = new HashMap<>();
	
	/**
	 * Add an attribute to this object
	 * @param attr - The {@link JsonAttribute}
	 */
	void addAttribute(JsonAttribute attr) {
		attributes.put(attr.getName(), attr);
	}
	
	/**
	 * Add an attribute to this object creating an new instance of {@link JsonAttribute}
	 * @param name - The name of the attribute
	 * @param value - The value of the atrtibute
	 */
	void addAttribute(String name, JsonValue value){
		attributes.put(name, new JsonAttribute(name, value));
	}
	
	/**
	 * Return an specific {@link JsonAttribute} by its name
	 * @param name
	 * @return
	 */
	public JsonAttribute getAttribute(String name) {
		return this.attributes.get(name);
	}
	
	public <T> T to(Class<? extends T> type) {
		T objectInstance = null;
		try {
			objectInstance = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		this.to(objectInstance);
		
		return objectInstance;
	}
	
	public void to(Object object) {
		JsonSerializer.jsonObjectToObject(this, object);
	}

	/**
	 * Return the JSON string representation of this object
	 */
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder("{") ;
		Set<String> attrsKeySet = attributes.keySet();
		int count = attrsKeySet.size();
		int currentIndex = 0;
		for (String attrName : attrsKeySet) {
			strBuilder.append(attributes.get(attrName).toString());
			if(currentIndex < count - 1) {
				strBuilder.append(",");
			}
			currentIndex++;
		}
		
		strBuilder.append("}");
		
		return strBuilder.toString();
	}
}
