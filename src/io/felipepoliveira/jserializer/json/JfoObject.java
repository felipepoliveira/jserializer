package io.felipepoliveira.jserializer.json;

import java.util.HashSet;
import java.util.Set;

import io.felipepoliveira.jserializer.exceptions.JfoParseException;
import io.felipepoliveira.jserializer.exceptions.JsonParseException;

/**
 * Class that represents an JSON Filter Object
 * @author Felipe Oliveira
 *
 */
public class JfoObject extends JsonObject{
	
	//Default constructor
	public JfoObject() {}
	
	private JfoFieldFilters fieldFilter = null;
	
	private Set<String> filteredFields;
	
	public JfoObject(JsonObject json) throws JfoParseException {
		fromJson(json);
	}
	
	private void fromJson(JsonObject object) throws JfoParseException{
		
		//Get the 'require' field
		JsonAttribute attr = object.getAttribute("require");
		if(attr != null) {
			validateFilteredFieldsAttribute(attr);
			fieldFilter = JfoFieldFilters.REQUIRE;
			filteredFields = getFieldsFromAttributeAsSet(attr);
			 
		}
		//Or the 'exclude' field
		else if((attr = object.getAttribute("exclude")) != null) {
			validateFilteredFieldsAttribute(attr);
			fieldFilter = JfoFieldFilters.EXCLUDE;
			filteredFields = getFieldsFromAttributeAsSet(attr);
		}else {
			throw new JsonParseException("Neither 'require' or 'exclude' filter fields was informmed in " + object.toString());
		}
	}
	
	private void validateFilteredFieldsAttribute(JsonAttribute attr) {
		//Check for possible errors
		if(!attr.getValue().isJsonArray()) {
			throw new JfoParseException("The filtered field attibute '"+attr.getName()+"' must be an array");
		}
	}
	
	private Set<String> getFieldsFromAttributeAsSet(JsonAttribute attr){
		Set<String> fields = new HashSet<>();
		
		for (JsonValue value : attr.getValue().asJsonArray().getValues()) {
			fields.add(value.asString());
		}
		
		return fields;
	}
	
	public Set<String> getFilteredFields() {
		return filteredFields;
	}
	
	public JfoFieldFilters getFieldFilter() {
		return fieldFilter;
	}
	
	public JsonFieldAccesTypes getFieldFilterAsJsonFieldAccessType() {
		switch (this.fieldFilter) {
		case EXCLUDE:
			return JsonFieldAccesTypes.EXCLUDE;

		case REQUIRE:
			return JsonFieldAccesTypes.INCLUDE;
		}
		
		return null;
	}
}
