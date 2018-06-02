package io.felipepoliveira.jserializer.json;

import io.felipepoliveira.jserializer.exceptions.JsonParseException;
import io.felipepoliveira.jserializer.utils.StringBuffer;

public class DefaultJsonParser implements JsonParser{

	@Override
	public JsonStructure parse(String json) throws JsonParseException {
		
		//Create the string buffer and go to the first value
		StringBuffer buffer = new StringBuffer(json);
		buffer.skipBlanks();
		
		//Check if the first value is an object or array
		if(!buffer.isOnContent() || (!buffer.isOn('{') && !buffer.isOn('['))) {
			throw new JsonParseException("Fail to parse the JSON string \"" + json + "\". The JSON must start with an object or array");
		}else if(buffer.isOn('{')) {
			return readObject(buffer);
		}else{
			return readArray(buffer); 
		}
	}
	
	public JsonObject readObject(StringBuffer buffer) throws JsonParseException {
		
		//Check for valid start of JSON object
		if(!buffer.isOnContent() || !buffer.skipBlanksUntilFind('{')) {
			throw new JsonParseException("Expecting the start of scope of an JSON object '{' got an '" + buffer.currentOrEndOfBuffer() + "' on " + buffer.toString());
		}
		
		//Create the JSON object
		JsonObject object = new JsonObject();
		
		//Start to read the first attributes
		while(buffer.next()) {
			
			//Skip until the first attribute
			buffer.skipBlanks();
			
			//If find an attribute '"'...
			if(buffer.isOn('"')) {
				readAttribute(object, buffer);
			}
			
			//Check if the object como till end
			if(buffer.isOn('}')) {
				break;
			}
		}
		
		return object;
	}
	
	public JsonValue readValue(StringBuffer buffer) throws JsonParseException {
		
		Object attrVal = null;
		//If is a string....
		if(buffer.isOn('"')) {
			attrVal = buffer.readUntil('"', true);
			buffer.next();
			
		//If is a object...
		}else if(buffer.isOn('{')) {
			attrVal = readObject(buffer);
			buffer.next();
		}else if(buffer.isOn('[')) {
			attrVal = readArray(buffer);
			buffer.next();
		}
		//Otherwise, read the content until the end of the value...
		else {
			attrVal = buffer.readUntilBeforeBlankOr(',', '}', ']');
		}
		
		//Create the json value....
		JsonValue value = null;
		
		//If the attribute value is a String, try to parse it...
		if((attrVal instanceof String)) {
			value = JsonValue.parse((String)attrVal);
		}else {
			//Otherwise, use it as the value of the Json Value
			value = new JsonValue(attrVal);
		}
		
		return value;
	}
	
	public JsonArray readArray(StringBuffer buffer) throws JsonParseException {
		
		//Check for valid begin of JSON array
		if(!buffer.isOnContent() || !buffer.skipBlanksUntilFind('[')) {
			throw new JsonParseException("Expecting to find begin of JSON array '[', found '" + buffer.currentOrEndOfBuffer() + "' instead at" + buffer.toString());
		}
		
		//Fo to the char after '['
		buffer.next();
		
		//Create the JSON array
		JsonArray array = new JsonArray();
		
		//Check for empty array
		if(buffer.isOn(']')) {
			return array;
		}
		do {
			buffer.skipBlanks();
			JsonValue value = readValue(buffer);
			array.addValue(value);
			buffer.skipBlanks();
			
			if(buffer.isOn(',')) {
				buffer.next();
				buffer.skipBlanks();
				continue;
			}
			
			
		}while(buffer.isOnContent() && !buffer.isOn(']'));
		
		return array;
	}
	
	public JsonAttribute readAttribute(JsonObject object, StringBuffer buffer) throws JsonParseException {
		//read the content until the next '"'
		buffer.next();
		String attrName = buffer.readUntilBefore('"', true);
		
		//Skip the last '"' occurrence
		buffer.next();
		
		//Try to to find the ':' occurrence
		if(!buffer.skipBlanksUntilFind(':')) {
			throw new JsonParseException("Expecting the ':' attribute value separator, got an '" + buffer.currentOrEndOfBuffer() + " in " + buffer.toString());
		}
		
		//Skip blanks until the next value
		buffer.next();
		buffer.skipBlanks();
		
		
		//Check the possible values of the attribute....
		JsonAttribute attr = new JsonAttribute(attrName, null);		
		
		//Read the value
		attr.setValue(readValue(buffer));
		
		//Add the JSON attribute in the object
		object.addAttribute(attr);
		
		//Skip all the blanks
		buffer.skipBlanks();
		
		return attr;
	}
}
