package io.felipepoliveira.jserializer.tests;

import java.util.ArrayList;
import java.util.Collection;

public class Address {
	
	private String street;
	
	public String otherField;
	
	public Collection<String> uhuuul;
	
	public Address() {
		uhuuul = new ArrayList<>();
		uhuuul.add("1");
		uhuuul.add("2");
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	

}
