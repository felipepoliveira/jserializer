package io.felipepoliveira.jserializer.json;

import io.felipepoliveira.jserializer.exceptions.JsonParseException;

public interface JsonParser {
	
	/**
	 * Parse an given JSON string to a generic type {@link JsonStructure}. This generic type could be
	 * a instance of {@link JsonObject} or {@link JsonArray} depending of the JSON inside the parameter
	 * @param json - An valid JSON representation. It could be a JSON object or array
	 * @return
	 * @throws JsonParseException Case the parser can not convert the given json string
	 */
	public JsonStructure parse(String json) throws JsonParseException;
}
