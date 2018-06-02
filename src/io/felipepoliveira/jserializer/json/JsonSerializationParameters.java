package io.felipepoliveira.jserializer.json;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JsonSerializationParameters {
	
	private Set<String> fields = new HashSet<>(5);
	
	private JsonFieldAccesTypes type = JsonFieldAccesTypes.INCLUDE;
	
	
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

}
