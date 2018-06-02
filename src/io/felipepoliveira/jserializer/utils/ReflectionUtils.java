package io.felipepoliveira.jserializer.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;

public class ReflectionUtils {
	
	public static Annotation getAnnotationOrNullOf(AccessibleObject accessibleObject, Class<? extends Annotation> annotationType) {
		for (Annotation a : accessibleObject.getAnnotations()) {
			if(annotationType.isInstance(a)) {
				return a;
			}
		}
		
		return null;
	}
	
	/**
	 * Return all fields of a type including the fields inherit by its parent classes recursively.
	 * This method will return the fields in an Map<String, Field> where:<br/>
	 * <b>key</b>: The name of the field<br/>
	 * <b>value</b>: The instance of the {@link Field}
	 * @param clazz
	 * @return
	 */
	public static Map<String, Field> getDeclaredFieldsIncludingSuperclassesAsMap(Class<? extends Object> clazz){ 
		return doGetDeclaredFieldsIncludingSuperclassesAsMap(new HashMap<>(10), clazz);
	}
	
	/**
	 * Return all methods  of a type including the methods inherit by its parent classes recursively.
	 * This method will return the fields in an Map<String, Field> where:<br/>
	 * <b>key</b>: The name of the field<br/>
	 * <b>value</b>: The instance of the {@link Field}
	 * @param clazz
	 * @return
	 */
	public static Map<String, Method> getDeclaredMethodsIncludingSuperclassesAsMap(Class<? extends Object> clazz){
		return doGetDeclaredMethodsIncludingSuperclassesAsMap(new HashMap<String, Method>(), clazz);
	}
	
	private static Map<String, Field> doGetDeclaredFieldsIncludingSuperclassesAsMap(Map<String, Field> currentAddedFields, Class<? extends Object> clazz){
		//Add all fields from the current class
		for (Field field : clazz.getDeclaredFields()) {
			currentAddedFields.put(field.getName(), field);
		}
		
		//Check if the current class has an parent class, read all fields from it recursively
		if(clazz.getSuperclass() != null) {
			doGetDeclaredFieldsIncludingSuperclassesAsMap(currentAddedFields, clazz.getSuperclass());
		}
		
		//Return the list
		return currentAddedFields;
	}
	
	private static Map<String, Method> doGetDeclaredMethodsIncludingSuperclassesAsMap(Map<String, Method> currentAddedFields, Class<? extends Object> clazz){
		//Add all methods from the current class
		for (Method method : clazz.getDeclaredMethods()) {
			currentAddedFields.put(method.getName(), method);
		}
		
		//Check if the current class has an parent class, read all methods  from it recursively
		if(clazz.getSuperclass() != null) {
			doGetDeclaredMethodsIncludingSuperclassesAsMap(currentAddedFields, clazz.getSuperclass());
		}
		
		//Return the list
		return currentAddedFields;
	}
	
	/**
	 * Return all fields of a type including the fields inherit by its parent classes recursively.
	 * This method will return the fields in an Map<String, Field> where:<br/>
	 * <b>key</b>: The name of the field<br/>
	 * <b>value</b>: The instance of the {@link Field}
	 * @param clazz
	 * @return
	 */
	public static Collection<Field> getDeclaredFieldsIncludingSuperclasses(Class<? extends Object> clazz){ 
		return doGetDeclaredFieldsIncludingSuperclasses(new ArrayList<>(10), clazz);
	}
	
	/**
	 * Return all methods  of a type including the methods inherit by its parent classes recursively.
	 * This method will return the fields in an Map<String, Field> where:<br/>
	 * <b>key</b>: The name of the field<br/>
	 * <b>value</b>: The instance of the {@link Field}
	 * @param clazz
	 * @return
	 */
	public static Collection<Method> getDeclaredMethodsIncludingSuperclasses(Class<? extends Object> clazz){
		return doGetDeclaredMethodsIncludingSuperclasses(new ArrayList<>(10), clazz);
	}
	
	private static Collection<Field> doGetDeclaredFieldsIncludingSuperclasses(Collection<Field> currentAddedFields, Class<? extends Object> clazz){
		//Add all fields from the current class
		currentAddedFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		
		//Check if the current class has an parent class, read all fields from it recursively
		if(clazz.getSuperclass() != null) {
			doGetDeclaredFieldsIncludingSuperclasses(currentAddedFields, clazz.getSuperclass());
		}
		
		//Return the list
		return currentAddedFields;
	}
	
	private static Collection<Method> doGetDeclaredMethodsIncludingSuperclasses(Collection<Method> currentAddedFields, Class<? extends Object> clazz){
		//Add all methods from the current class
		currentAddedFields.addAll(Arrays.asList(clazz.getDeclaredMethods()));
		
		//Check if the current class has an parent class, read all methods  from it recursively
		if(clazz.getSuperclass() != null) {
			doGetDeclaredMethodsIncludingSuperclasses(currentAddedFields, clazz.getSuperclass());
		}
		
		//Return the list
		return currentAddedFields;
	}
	
	/**
	 * Invoke the get method of an given field. If the get method is not declared, it returns
	 * the value of the field
	 * @param obj - The object that will have its field read
	 * @param field - The field
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object invokeGetMethodOrGetFieldValue(Object obj, Field field) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//Try to find the get method of the object
		try {
			Method getMethod = findGetMethodOf(obj.getClass(), field);
			return getMethod.invoke(obj);
		} catch (NoSuchMethodException | SecurityException e) {}
		
		return field.get(obj);
	}
	
	public static void invokeSetMethodOrSetValue(Object obj, Field field, Object value) {
		//Try to find the set method of the object
		try {
			Method setMethod = findSetMethodOf(obj.getClass(), field);
			try {
				setMethod.invoke(obj, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		} catch (NoSuchMethodException | SecurityException e) {
			try {
				field.set(obj, value);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				throw new RuntimeException(e1);
			}
		}
		
		
	}
	
	/**
	 * Find an get method of an given property name
	 * @param clazz
	 * @param propName - The name of the property
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static Method findGetMethodOf(Class<? extends Object> clazz, Field field) throws NoSuchMethodException, SecurityException {
		return getDeclaredMethodsIncludingSuperclassesAsMap(clazz).get("get" + StringUtils.capitalize(field.getName()));
	}
	
	/**
	 * Find an get method of an given property name
	 * @param clazz
	 * @param propName - The name of the property
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static Method findSetMethodOf(Class<? extends Object> clazz, Field field) throws NoSuchMethodException, SecurityException {
		return getDeclaredMethodsIncludingSuperclassesAsMap(clazz).get("set" + StringUtils.capitalize(field.getName()));
	}
	
	/**
	 * Check if an given class contains an get method
	 * @param clazz - The class
	 * @param propName - The name of the property
	 * @return
	 * @throws SecurityException
	 */
	public static boolean getMethodExists(Class<? extends Object> clazz, Field field) throws SecurityException {
		try {
			clazz.getMethod("get" + StringUtils.capitalize(field.getName()));
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}
}
