package io.felipepoliveira.jserializer.json;

import java.lang.reflect.Array;
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
	
	/**
	 * Put the values of this {@link JsonArray} instance into a new instance
	 * of an given type
	 * @param type - The type to be parsed
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] to(Class<? extends T> type) {
		//Create the instance of the object array
		T[] objectInstances = (T[]) Array.newInstance(type, this.values.size());
		
		//Iterate over the values, converting each element as json object
		int index = 0;
		for (JsonValue value : this.values) {
			objectInstances[index] = value.asJsonObject().to(type);
			index++;
		}
		
		return objectInstances;
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
