package io.felipepoliveira.jserializer.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import io.felipepoliveira.jserializer.ClassMetadata;
import io.felipepoliveira.jserializer.ClassMetadataContext;
import io.felipepoliveira.jserializer.SerializationField;
import io.felipepoliveira.jserializer.exceptions.JsonParseException;
import io.felipepoliveira.jserializer.exceptions.UnreadableFieldException;

public class JsonSerializer{
	
	private static final JsonSerializationParameters defaultEmptyParameters = new JsonSerializationParameters();
	
	/**
	 * Define the JsonParser type used in the {@link JsonSerializer}.parser object.
	 * An new instance of this type is made in the JsonSerializer() constructor method.
	 */
	public static Class<? extends JsonParser> jsonParserType = DefaultJsonParser.class;
	
	/**
	 * The JSON string parser
	 */
	private static JsonParser parser;
	
	public JsonSerializer() {
		try {
			parser = JsonSerializer.jsonParserType.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new ClassCastException("Invalid JsonParser type used: " + jsonParserType);
		}
	}
	
	/**
	 * Put all the values in an {@link JsonObject} to an {@link Object} using reflection.
	 * The criteria used to put the values from one object to another is when the
	 * {@link JsonValue} attribute match with the name of the {@link Field} of the {@link Object}
	 * @param jsonObject - The JSON object with the data
	 * @param object - The object that will receive the data
	 */
	public static void jsonObjectToObject(JsonObject jsonObject, Object object) {
		
		//Get the object metadata
		ClassMetadata metadata = ClassMetadataContext.get(object.getClass());
		
		//get each field of the object
		for (String fname : metadata.getFields().keySet()) {
			
			//Try to find the attribute in the json object
			JsonAttribute jsonAttribute = jsonObject.getAttribute(fname);
			
			//If the JSON attribute does not exists, go to the next one
			if(jsonAttribute == null) {
				continue;
			}
			
			//get the serialization field
			SerializationField field = metadata.getFields().get(fname);
			
			//Check if field has authority to write the value (from @SerializationAccess(writable (bool))
			if(!field.isWriteable()) {
				continue;
			}
			
			//Apply the json value into the object
			applyValue(jsonAttribute.getValue(), object, field);
		}
	}
	
	/**
	 * Apply an value from {@link JsonValue} to an specific {@link Field} of a object
	 * @param value - The value to applied
	 * @param object - The object that will receive the value
	 * @param field - The field to put the value
	 */
	private static void applyValue(JsonValue value, Object object, SerializationField field) {
		//Check if the current field is not an object
		if(		!ifInnerObject(value, object, field) &&
				!ifInnerArray(value, object, field) &&
				!ifInnerCollection(value, object, field) &&
				!ifInnerMap(value, object, field)) {
			try {
				//Check if the current object is not a json raw data (is a Object)
				if(!JsonValue.isJsonRawData(object)) {
					field.setValueFromSetMethodOrField(object, adaptJsonValueToObjectField(value, field.getField()));
				}
				//Otherwise, put the value from the field directly in the object
				else {
					object = value.getOriginalValue();
				}
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Get each value from an {@link JsonArray} and apply it in an given object array.
	 * This object array must have the same number of elements of the given {@link JsonArray}.<br/>
	 * @param jsonArray - The JSON Array with the values
	 * @param target - The object[] that will receive the values
	 * @param type - The type of object to be self instantiated (with reflection) in the array
	 * @param field - In witch field the array must be applied
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private static void applyIterableValuesInArray(JsonArray jsonArray, Object[] target, Class<?> type, SerializationField field) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Iterator<JsonValue> iJsonArray = jsonArray.getValues().iterator();
		
		int index = -1;
		while(iJsonArray.hasNext()) {
			index++;
			JsonValue jsonValue = iJsonArray.next();
			target[index] = type.getConstructor().newInstance();
			
			applyValue(jsonValue, target[index], field);
		}
	}
	
	/**
	 * Get each value from an {@link JsonArray} and apply it in an given collection of objects.
	 * @param jsonArray - The JSON Array with the values
	 * @param target - The Collection that will receive the values
	 * @param type - The type of object to be self instantiated (with reflection) in the array
	 * @param field - In witch field the array must be applied
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private static void applyIterableValuesInCollection(JsonArray jsonArray, Collection<Object> target, Class<?> type, SerializationField field) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Iterator<JsonValue> iJsonArray = jsonArray.getValues().iterator();
		
		while(iJsonArray.hasNext()) {
			JsonValue jsonValue = iJsonArray.next();
			Object targetValue = type.getConstructor().newInstance();
			applyValue(jsonValue, targetValue, field);
			target.add(targetValue);
		}
	}
	
	/**
	 * This method adapt the value that will be applied in an given field
	 * @param value
	 * @param targetField
	 * @return
	 */
	private static Object adaptJsonValueToObjectField(JsonValue value, Field targetField) {
		//If the value is a String, call the JsonValue.asString() method to remove the escaped characters
		if(!value.isNull() && String.class.isAssignableFrom(targetField.getType())) {
			return value.asString();
		}
		//If is not an special case, get the original value directly
		else {
			return value.getOriginalValue();
		}
	}
	
	/**
	 * Check if he current {@link JsonValue} and the {@link SerializationField} of an {@link Object} is
	 * a inner object. If it is, make an internal instance of this object to attribute the values of
	 * the {@link JsonValue} in it
	 * @param jsonValue - The current {@link JsonValue}. This value should be an object.
	 * @param object - The object owner of the field to be defined. This object must not be an JSON raw data
	 * @param field - The field that will receive the value from the {@link JsonValue}
	 * @return Flag indicating if the current field is or not an object
	 */
	private static boolean ifInnerObject(JsonValue jsonValue, Object object, SerializationField field) {
		//Check if the attribute is a object and the field is not primitive
		if(jsonValue.isJsonObject() && !JsonValue.isJsonRawData(object)) {
			
				
			//Get the field value
			Object fieldValue = null;
			try {
				 fieldValue = field.getValueFromGetMethodOrField(object);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
				
				
			//if field is null, make a new instance
			if(fieldValue == null) {
				try {
					fieldValue = field.getField().getType().getConstructor().newInstance();
						field.setValueFromSetMethodOrField(object, fieldValue);
					
					
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException | NoSuchMethodException | SecurityException e) {
					throw new RuntimeException("Can not make an internal instance of " + object.getClass().getSimpleName() + "." + field.getField().getName());
				}

			}
			
			//Call the recursive method
			jsonObjectToObject(jsonValue.asJsonObject(), fieldValue);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if he current {@link JsonValue} and the {@link SerializationField} of an {@link Object} is
	 * a inner Array. If it is, make an internal instance of this array to attribute the values of
	 * the {@link JsonValue} in it
	 * @param jsonValue - The current {@link JsonValue}. This value should be an object.
	 * @param object - The object owner of the field to be defined. This object must not be an JSON raw data
	 * @param field - The field that will receive the value from the {@link JsonValue}
	 * @return Flag indicating if the current field is or not an object
	 */
	private static boolean ifInnerArray(JsonValue jsonValue, Object object, SerializationField field) {
		
		if(jsonValue.isJsonArray() && field.getField().getType().isArray()) {
			System.out.println("Array detected");
			
			//Get the value as JsonArray
			JsonArray jsonArray = jsonValue.asJsonArray();
			
			//Get the array type
			Class<?> arrayType = field.getField().getType().getComponentType();
			
			//Create an instance of the inner array
			try {
				field.setValueFromSetMethodOrField(object, Array.newInstance(arrayType, jsonArray.getValues().size()));
				//Get each element in JsonArray and apply it on each element in the object
				applyIterableValuesInArray(jsonArray, (Object[]) field.getValueFromGetMethodOrField(object), arrayType, field);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NegativeArraySizeException | InstantiationException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
			
			return true;
		}else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static boolean ifInnerCollection(JsonValue jsonValue, Object object, SerializationField field) {
		
			
		if(jsonValue.isJsonArray() && Collection.class.isAssignableFrom(field.getField().getType())) {
			System.out.println("Collection detected");
			
			
			//Get the generic type inside the collection type
			String genericCollectionTypeName = field.getField().getGenericType().toString();
			
			//if the colleciton does not have a generic type declared, it will be ignored because it can't be self instantiated
			if(!genericCollectionTypeName.contains("<")) {
				return false;
			}
			
			//get the generic type by class name
			genericCollectionTypeName = genericCollectionTypeName.substring(genericCollectionTypeName.indexOf("<") + 1, genericCollectionTypeName.indexOf(">"));
			Class<?> collectionType = null;
			try {
				 collectionType = Class.forName(genericCollectionTypeName);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("The type " + genericCollectionTypeName + " of the collection " + field.getField() + " was not found");
			}
			
			//Get the value as JsonArray
			JsonArray jsonArray = jsonValue.asJsonArray();
			
			
			try {
				field.setValueFromSetMethodOrField(object, new ArrayList<>());
				applyIterableValuesInCollection(jsonArray, (Collection<Object>)field.getValueFromGetMethodOrField(object), collectionType, field);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
			
			return true;
		}
		else {
			return false;
		}
		
	}
	
	private static boolean ifInnerMap(JsonValue jsonValue, Object object, SerializationField field) {
		if(field.getField().getType().equals(Map.class)) {
			System.out.println("Map detected");
			return true;
		}else {
			return false;
		}
	}
	
	private static boolean isArray(SerializationField field) {
		return (field.getField().getType().isArray());
	}
	
	private static boolean isCollection(SerializationField field) {
		return (Collection.class.isAssignableFrom(field.getField().getType())); 
	}
	
	
	/**
	 * Parse an given JSON string to an {@link JsonStructure} instance. The instance can be of a
	 * {@link JsonArray} or {@link JsonObject} depending of the string used in the parameter
	 * @param json - The string to parse
	 * @return
	 * @throws JsonParseException
	 */
	public JsonStructure parse(String json) throws JsonParseException {
		return parser.parse(json);
	}
	
	public JfoObject parseJfo(String jfo) {
		return new JfoObject(parser.parse(jfo).asJsonObject());
	}
	
	public JsonArray serialize(Object[] objects, JsonSerializationParameters parameters) {
		JsonArray jsonArray = new JsonArray();
		
		//Get each object from the array, serialize it and add in array
		for (Object object : objects) {
			jsonArray.addValue(serialize(object, parameters));
		}
		
		return jsonArray;
	}
	
	public JsonArray serialize(Collection<Object> objects, JsonSerializationParameters parameters) {
		JsonArray jsonArray = new JsonArray();
		
		//Get each object from the array, serialize it and add in array
		for (Object object : objects) {
			jsonArray.addValue(serialize(object, parameters));
		}
		
		return jsonArray;
	}

	@SuppressWarnings("unchecked")
	public JsonObject serialize(Object object, JsonSerializationParameters parameters) {
		JsonObject jsonObject = new JsonObject();
		
		Object fvalue = null;
		
		ClassMetadata metadata = ClassMetadataContext.get(object.getClass());
		
		//Get all fields from the object class (including from the superclasses...)
		for (String fname : metadata.getFields().keySet()) {
		
			
			//Check if the serialization parameters has specific fields to serialize
			if(parameters.hasFields()) {
				//Check if is on include mode and the current field is not included
				if(parameters.getType() == JsonFieldAccesTypes.INCLUDE) {
					if(!parameters.containsField(fname)) {
						continue;
					}
				}else {
					if(parameters.containsField(fname)) {
						continue;
					}
				}
			}
			
			//Get serialization field form metadata
			SerializationField field = metadata.getFields().get(fname);
			
			//Check in the ClassMetadata if the field has authority to read. The authority is defined
			//by the @SerializationAccess(read (boolean)) annotation
			if(!field.isReadable() || !field.isAccessible()) {
				continue;
			}
			
			//Try to read the value
			try {
				fvalue = field.getValueFromGetMethodOrField(object);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new UnreadableFieldException(e);
			}
			
			//If the object is from Object type call this method recursively
//			if(fvalue != null && !JsonValue.isJsonRawData(fvalue)) {
//				fvalue = serialize(fvalue, parameters.createParametersDerivedFrom(fname));
//			}
			
			
			if(fvalue != null) {
				
				//Check if is an collection
				if(isArray(field) || isCollection(field)) {
					JsonArray jsonArray = new JsonArray();
					//Check if is array
					if(isArray(field)) {
						for(Object collectionValue : (Object[]) fvalue) {
							
							if(!JsonValue.isJsonRawData(collectionValue)) {
								jsonArray.addValue(serialize(collectionValue, parameters.createParametersDerivedFrom(fname)));
							}else {
								jsonArray.addValue(fvalue);
							}
							
						}
					}
					//Check if collection
					else {
						for(Object collectionValue : (Collection<Object>) fvalue) {
							if(!JsonValue.isJsonRawData(collectionValue)) {
								jsonArray.addValue(serialize(collectionValue, parameters.createParametersDerivedFrom(fname)));
							}else {
								jsonArray.addValue(fvalue);
							}
						}
					}
					
					fvalue = jsonArray;
				}
				//If is not a collection or array
				else {
					if(!JsonValue.isJsonRawData(fvalue)) {
						fvalue = serialize(fvalue, parameters.createParametersDerivedFrom(fname));
					}
				}
			}
			
			
						
			//Create the json attribute with the field name and value
			jsonObject.addAttribute(fname, new JsonValue(fvalue));
			
		}
		
		return jsonObject;
	}
	
	public JsonObject serialize(Object object) {
		return serialize(object, defaultEmptyParameters);
	}
}
