package io.felipepoliveira.jserializer.json;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JsonSerializationParameters {
	
	private Set<String> fields = new HashSet<>(5);
	
	private JsonFieldAccesTypes type = null;
	
	public JsonSerializationParameters createParametersDerivedFrom(String field) {
		JsonSerializationParameters parameters = new JsonSerializationParameters();
		
		//Pass this type to the internal parameters
		parameters.setType(this.type);
		
		for (String thisField : fields) {
			//Check if the field starts with the given field name and a field separator '.'
			if(thisField.startsWith(field + ".")) {
				//Add the field removing the first prefix
				parameters.fields.add(thisField.substring(field.length() + 1, thisField.length()));
			}
		}
		
		return parameters;
	}
	
	public boolean isParametrized() {
		return this.type != null;
	}
	
	
	public boolean hasFields() {
		return !fields.isEmpty();
	}
	
	public Set<String> getFields() {
		return fields;
	}
	
	public void setFields(Set<String> fields) {
		this.fields = fields;
	}
	
	public void setFields(String... fields) {
		this.fields.clear();
		this.fields.addAll(Arrays.asList(fields));
	}
	
	public JsonFieldAccesTypes getType() {
		return type;
	}
	
	public void setType(JsonFieldAccesTypes type) {
		this.type = type;
	}
	
	public boolean containsField(String field) {
		for (String thisFields : fields) {
			if(field.equals(thisFields) || thisFields.startsWith(field + ".")) {
				return true;
			}
		}
		
		return false;
	}

}
