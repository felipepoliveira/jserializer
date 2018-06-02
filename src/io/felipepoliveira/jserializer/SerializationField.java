package io.felipepoliveira.jserializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SerializationField {
	
	private Field field;
	
	private Method getMethod;
	
	private Method setMethod;
	
	public SerializationField(Field field, Method getMethod, Method setMethod) {
		this.field = field;
		this.getMethod = getMethod;
		this.setMethod = setMethod;
	}
	
	public void setValueFromSetMethodOrField(Object source, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(setMethod != null) {
			setMethod.invoke(source, value);
		}else {
			field.set(source, value);
		}
	}
	
	public Object getValueFromGetMethodOrField(Object source) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(getMethod != null) {
			return getMethod.invoke(source);
		}else {
			return this.field.get(source);
		}
	}

	public Method getGetMethod() {
		return getMethod;
	}
	
	public Field getField() {
		return field;
	}
}
