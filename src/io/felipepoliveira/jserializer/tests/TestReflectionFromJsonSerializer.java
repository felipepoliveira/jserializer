package io.felipepoliveira.jserializer.tests;

import io.felipepoliveira.jserializer.JSerializer;
import io.felipepoliveira.jserializer.json.JsonStructure;
import io.felipepoliveira.jserializer.json.JsonFieldAccesTypes;
import io.felipepoliveira.jserializer.json.JsonObject;

public class TestReflectionFromJsonSerializer {

	public static void main(String[] args) {
		
		Client client = new Client();
		client.setId(1L);
		client.setName("meu nome é Felipe");
		
		Address a = new Address();
		a.setStreet("Rua");
		client.setAddress(a);
		
		long start = System.currentTimeMillis();
		System.out.println(
		JSerializer
			.json()
			.withFields(JsonFieldAccesTypes.EXCLUDE, "name", "id")
			.serialize(client));
		System.out.println(System.currentTimeMillis() - start);
		
		Client parsedClient =
		JSerializer
			.json()
				.parse("{\"address\" : {\"street\" : \"Rua das Flores\"}}")
				.asJsonObject()
				.to(Client.class);
		System.out.println(parsedClient.getAddress().getStreet());
		
			JsonObject object =  JSerializer.json().parse("{\"require\" : [\"nome\", \"address\"], \"id\" : \"16/05/1995\"}").asJsonObject();
			JsonStructure data = JSerializer.json().parse("{}");
//			JsonArray array = JSerializer.json().parseArray("         []");
			start = System.currentTimeMillis();
			System.out.println(object);
			System.out.println(System.currentTimeMillis() - start);

	}

}
