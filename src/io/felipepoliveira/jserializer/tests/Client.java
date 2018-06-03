package io.felipepoliveira.jserializer.tests;

import io.felipepoliveira.jserializer.SerializationAccess;

public class Client extends People{
	
	private Long registrationNumber;
	
	private Address address;
	
	@SerializationAccess(readable = false)
	private Client[] children = new Client[5];
	
	public Client() {
		
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
	
	public Client[] getChildren() {
		return children;
	}

	public void setChildren(Client[] children) {
		this.children = children;
	}
}
