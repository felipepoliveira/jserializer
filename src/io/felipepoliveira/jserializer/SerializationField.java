package io.felipepoliveira.jserializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SerializationField {
	
	private Field field;
	
	private Method getMethod;
	
	private Method setMethod;
	
	private boolean isReadable = true;
	
	private boolean isWriteable = true;
		
	public SerializationField(Field field, Method getMethod, Method setMethod) {
		this.field = field;
		this.getMethod = getMethod;
		this.setMethod = setMethod;
		checkIfIsReadableAndWritable();
	}
	
	private void checkIfIsReadableAndWritable() {
		
		//Get each annotation and check if it has SerializationAccess
		for (Annotation ann : field.getAnnotations()) {
			if(ann instanceof SerializationAccess) {
				isReadable = ((SerializationAccess) ann).readable();
				isWriteable = ((SerializationAccess) ann).writable();
				break;
			}
		}
		
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
	
	public boolean isReadable() {
		return isReadable;
	}
	
	public boolean isWriteable() {
		return isWriteable;
	}
	
	public boolean isAccessible() {
		return (field.isAccessible() || getMethod != null && getMethod.isAccessible());
	}
	
	public boolean isRawData() {
		return (field.getType().isPrimitive() || (field.getType().equals(String.class)));
	}

}
