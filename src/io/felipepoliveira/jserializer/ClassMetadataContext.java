package io.felipepoliveira.jserializer;

import java.util.HashMap;
import java.util.Map;

public class ClassMetadataContext {
	
	private final static Map<Class<?>, ClassMetadata> metadatas = new HashMap<>();
	
	public static ClassMetadata get(Class<?> clazz) {
		ClassMetadata metadata = metadatas.get(clazz);
		if(metadata == null) {
			metadata = ClassMetadata.loadMetadata(clazz);
			metadatas.put(clazz, metadata);
		}
		
		return metadata;
	}

}
