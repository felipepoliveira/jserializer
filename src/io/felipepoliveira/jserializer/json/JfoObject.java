package io.felipepoliveira.jserializer.json;

import io.felipepoliveira.jserializer.exceptions.JfoParseException;

/**
 * Class that represents an JSON Filter Object
 * @author Felipe Oliveira
 *
 */
public class JfoObject extends JsonObject{
	
	//Default constructor
	public JfoObject() {}
	
	public JfoObject(JsonObject json) throws JfoParseException {
		fromJson(json);
	}
	
	private void fromJson(JsonObject object) throws JfoParseException{
		//Get the 'require' field
		JsonAttribute attr = object.getAttribute("require");
		if(attr != null) {
			
		}
	}
	
}
