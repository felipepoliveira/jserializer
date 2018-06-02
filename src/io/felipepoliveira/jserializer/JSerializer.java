package io.felipepoliveira.jserializer;

import io.felipepoliveira.jserializer.json.JsonSerializationBuilder;

public class JSerializer {
	
	/**
	 * Create an return an instance of the {@link JsonSerializationBuilder} object
	 * @return
	 */
	public static final JsonSerializationBuilder json() {
		return new JsonSerializationBuilder();
	}
}
