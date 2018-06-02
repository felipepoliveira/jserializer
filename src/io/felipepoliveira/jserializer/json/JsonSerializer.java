package io.felipepoliveira.jserializer.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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
			parser = JsonSerializer.jsonParserType.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ClassCastException("Invalid JsonParser type used: " + jsonParserType);
		}
	}
	
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
			
			//Check if the current field is not an object
			if(!ifInnerObjectCallJsonObjectToObject(jsonAttribute, object, field)) {
				try {
					field.setValueFromSetMethodOrField(object, adaptJsonValueToObjectField(jsonAttribute.getValue(), field.getField()));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	private static Object adaptJsonValueToObjectField(JsonValue value, Field targetField) {
		if(targetField.getType().equals(String.class)) {
			return value.asString();
		}else {
			return value.getOriginalValue();
		}
	}
	
	private static boolean ifInnerObjectCallJsonObjectToObject(JsonAttribute jsonAttribute, Object object, SerializationField field) {
		//Check if the attribute is a object and the field is not primitive
		if(jsonAttribute.getValue().isJsonObject() && !field.getField().getType().isPrimitive()) {
			
				
			//Get the field value
			Object fieldValue = null;
			try {
				 fieldValue = field.getValueFromGetMethodOrField(object);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
				
				
			//if field is null, make a new instance
			if(fieldValue == null) {
				try {
					fieldValue = field.getField().getType().newInstance();
					field.setValueFromSetMethodOrField(object, fieldValue);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					throw new RuntimeException("Can not make an internal instance of " + object.getClass().getSimpleName() + "." + field.getField().getName());
				}

			}
			
			//Call the recursive method
			jsonObjectToObject(jsonAttribute.getValue().asJsonObject(), fieldValue);
			
			return true;
		}
		
		return false;
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

	public JsonObject serialize(Object object, JsonSerializationParameters parameters) {
		JsonObject jsonObject = new JsonObject();
		
		Object fvalue = null;
		
		ClassMetadata metadata = ClassMetadataContext.get(object.getClass());
		
		//Get all fields from the object class (including from the superclasses...)
		for (String fname : metadata.getFields().keySet()) {
		
			
			//Check if the serialization parameters has specific fields to serialize
			if(parameters.isParametrized()) {
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
			if(!field.isReadable() && !field.isAccessible()) {
				continue;
			}
			
			//Try to read the value
			try {
				fvalue = field.getValueFromGetMethodOrField(object);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new UnreadableFieldException(e);
			}
			
			//If the object is from Object type call this method recursively
			if(fvalue != null && !JsonValue.isJsonRawData(fvalue)) {
				fvalue = serialize(fvalue, parameters.createParametersDerivedFrom(fname));
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
