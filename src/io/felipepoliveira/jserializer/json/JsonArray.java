package io.felipepoliveira.jserializer.json;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class JsonArray implements JsonStructure, JsonData {
	
	private List<JsonValue> values = new LinkedList<JsonValue>();
	
	public JsonArray() {}
	
	/**
	 * Create an JsonArray object from an source of values of an Object array
	 * @param values
	 */
	public JsonArray(Object[] values) {
		fromIterable(Arrays.asList(values));
	}
	
	/**
	 * Create an JsonArray object from an source of a collection
	 * @param values
	 */
	public JsonArray(Collection<Object> values) {
		fromIterable(values);
	}
	
	/**
	 * Add each value from the iterable in the internal {@link JsonValue} list of the array
	 * @param iterable
	 */
	private void fromIterable(Iterable<Object> iterable) {
		for (Object object : iterable) {
			this.values.add(new JsonValue(object));
		}
	}
	
	public <T> T[] to(Class<? extends T> type) {
		T[] objectInstance = null;
		
		return objectInstance;
	}
	
	/**
	 * Return the representation of an JSON Array in string [<value1>, <value2>, <value3>, ...]
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		//Starts with [
		builder.append("[");
		
		//Add each json value separating the values with ','
		int current = 0, size = values.size();
		for (JsonValue jsonValue : values) {
			builder.append(jsonValue.toString());
			if(current < size - 1) {
				builder.append(",");
			}
			
			current++;
		}
		
		//Close the array
		builder.append("]");
		
		return builder.toString();
	}
	
	/**
	 * Add an value to the JsonArray values list
	 * @param value
	 */
	void addValue(Object value) {
		values.add(new JsonValue(value));
	}
	
	/**
	 * Get the values from the Array
	 * @return
	 */
	public List<JsonValue> getValues() {
		return values;
	}
}
