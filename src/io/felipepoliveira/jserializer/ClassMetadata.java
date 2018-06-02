package io.felipepoliveira.jserializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.felipepoliveira.jserializer.utils.ReflectionUtils;

public class ClassMetadata {
	
	private Map<String, SerializationField> fields = new HashMap<>(8);
		
	
	public Map<String, SerializationField> getFields() {
		return fields;
	}
	
	public static ClassMetadata loadMetadata(Class<?> clazz) {
		
		ClassMetadata metadata = new ClassMetadata();
		
		//Get all fields from the object class (including from the superclasses...)
		for (Field field : ReflectionUtils.getDeclaredFieldsIncludingSuperclasses(clazz)) {
			
			addSerializationField(clazz, field, metadata);
		}
		
		return metadata;
	}
	
	private static void addSerializationField(Class<?> clazz, Field field, ClassMetadata metadata) {
		Method getMethod = null;
		Method setMethod = null;
		
		
		//Try to find the getMethod of the field
		try {
			getMethod = ReflectionUtils.findGetMethodOf(clazz, field);
		} catch (NoSuchMethodException | SecurityException e) {}
		
		//Try to find the set method of the field
		try {
			setMethod = ReflectionUtils.findSetMethodOf(clazz, field);
		} catch (NoSuchMethodException | SecurityException e) {}
		
		metadata.fields.put(field.getName(), new SerializationField(field, getMethod, setMethod));
		
	}
}
