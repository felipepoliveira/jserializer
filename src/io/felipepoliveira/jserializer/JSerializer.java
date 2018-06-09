package io.felipepoliveira.jserializer;

import io.felipepoliveira.jserializer.json.JsonSerializationBuilder;

public class JSerializer {
	
	private static SerializationConfiguration configuration = new SerializationConfiguration();
	
	/**
	 * Create an return an instance of the {@link JsonSerializationBuilder} object
	 * @return
	 */
	public static final JsonSerializationBuilder json() {
		return new JsonSerializationBuilder();
	}
	
	/**
	 * @return Return the global configuration object of the serialization
	 */
	public static final SerializationConfiguration configuration() {
		return configuration;
	}
}
