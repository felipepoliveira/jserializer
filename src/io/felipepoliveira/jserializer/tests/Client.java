package io.felipepoliveira.jserializer.tests;

import io.felipepoliveira.jserializer.SerializationAccess;

public class Client extends People{
	
	private Long registrationNumber;
	
	private Address address;
	
	@SerializationAccess(readable = false)
	private Object[] children = new Object[5];
	
	public Client() {
		children[0] = 1L;
		children[1] = 1;
		children[2] = new People();
		children[3] = null;
		children[4] = new People();
	}

	public Long getRegistrationNumber() {
		return registrationNumber;
	}


	public void setRegistrationNumber(Long registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Object[] getChildren() {
		return children;
	}

	public void setChildren(Object[] children) {
		this.children = children;
	}
}
