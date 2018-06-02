package io.felipepoliveira.jserializer.tests;

import io.felipepoliveira.jserializer.JSerializer;
import io.felipepoliveira.jserializer.json.JsonStructure;

public class TestReflectionFromJsonSerializer {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		String clientJson = "{\r\n" + 
				"	\"id\": 1,\r\n" +
				"	\"name\": \"Felipe Pereira de Oliveira\",\r\n" + 
				"	\"registrationNumber\": 11232,\r\n" + 
				"	\"address\": {\r\n" + 
				"		\"street\": \"Rua das Flores\"\r\n" + 
				"	}\r\n" + 
				"}";
		
		Client client = JSerializer.json().parse(clientJson).asJsonObject().to(Client.class);
		
		System.out.println(client.getName());
		
		JsonStructure json = 
		JSerializer.json()
			
			.serialize(client);
		
		System.out.println(json);
		

	}

}
