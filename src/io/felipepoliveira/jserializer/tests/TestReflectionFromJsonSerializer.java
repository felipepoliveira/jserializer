package io.felipepoliveira.jserializer.tests;

import io.felipepoliveira.jserializer.JSerializer;
import io.felipepoliveira.jserializer.json.JsonArray;
import io.felipepoliveira.jserializer.json.JsonObject;
import io.felipepoliveira.jserializer.json.JsonStructure;

public class TestReflectionFromJsonSerializer {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		String clientJson = "{\r\n" + 
				"    \"numeroOcorrencias\": 2,\r\n" + 
				"    \"numeroPaginas\": 1,\r\n" + 
				"    \"itensPorPagina\": 20,\r\n" + 
				"    \"paginaAtual\": 1,\r\n" + 
				"    \"itens\": [\r\n" + 
				"        {\r\n" + 
				"            \"id\": 29,\r\n" + 
				"            \"nome\": \"Carlos de Almeida\",\r\n" + 
				"            \"email\": \"contato@empresadafamilia.com\",\r\n" + 
				"            \"cpf\": \"12342142378\",\r\n" + 
				"            \"dataCadastro\": 1526296430000,\r\n" + 
				"            \"dataAtivacao\": null,\r\n" + 
				"            \"ativo\": true,\r\n" + 
				"            \"endereco\": {\r\n" + 
				"                \"id\": 30,\r\n" + 
				"                \"estado\": {\r\n" + 
				"                    \"id\": 11,\r\n" + 
				"                    \"uf\": \"MA\"\r\n" + 
				"                },\r\n" + 
				"                \"logradouro\": \"R. das Margaridas\",\r\n" + 
				"                \"bairro\": \"Guaianases\",\r\n" + 
				"                \"numero\": 65,\r\n" + 
				"                \"complemento\": null,\r\n" + 
				"                \"cep\": \"12345987\"\r\n" + 
				"            },\r\n" + 
				"            \"cnpj\": \"123694189512340\",\r\n" + 
				"            \"nomeFantasia\": \"Empresa da Família\",\r\n" + 
				"            \"cotaNotasAtual\": 0,\r\n" + 
				"            \"confirmado\": false,\r\n" + 
				"            \"name\": \"Carlos de Almeida\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"id\": 31,\r\n" + 
				"            \"nome\": \"Felipe Pereira de Oliveira\",\r\n" + 
				"            \"email\": \"felipeperoliveira@outlook.com\",\r\n" + 
				"            \"cpf\": \"12342142371\",\r\n" + 
				"            \"dataCadastro\": 1526296489000,\r\n" + 
				"            \"dataAtivacao\": null,\r\n" + 
				"            \"ativo\": true,\r\n" + 
				"            \"endereco\": {\r\n" + 
				"                \"id\": 32,\r\n" + 
				"                \"estado\": {\r\n" + 
				"                    \"id\": 11,\r\n" + 
				"                    \"uf\": \"MA\"\r\n" + 
				"                },\r\n" + 
				"                \"logradouro\": \"R. das Margaridas\",\r\n" + 
				"                \"bairro\": \"Guaianases\",\r\n" + 
				"                \"numero\": 65,\r\n" + 
				"                \"complemento\": null,\r\n" + 
				"                \"cep\": \"12345987\"\r\n" + 
				"            },\r\n" + 
				"            \"cnpj\": \"123694189512341\",\r\n" + 
				"            \"nomeFantasia\": \"Empresa da Família\",\r\n" + 
				"            \"cotaNotasAtual\": 0,\r\n" + 
				"            \"confirmado\": true,\r\n" + 
				"            \"name\": \"Felipe Pereira de Oliveira\"\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";
		
		long parsingTime = System.currentTimeMillis();
		JsonObject jsonObject =
		JSerializer.json()
			.parse(clientJson)
			.asJsonObject();
		System.out.println("Time used to parse the JSON: " + (System.currentTimeMillis() - parsingTime));
		
		
		//Client simulation
		Client client = new Client();
		client.setId(1L);
		client.setName("Felipe Pereira de Oliveira");
		client.setRegistrationNumber(38217392837L);
		
		//Address of Client
		Address address = new Address();
		address.setStreet("My Street");
		client.setAddress(address);
		
		//Try to serialize the client
		JsonObject jsonClient = 
		JSerializer.json()
			.serialize(client);
		
		System.out.println(jsonClient);
		
		//Client array simulation
		Client[] clients = new Client[3];
		clients[0] = client;
		clients[1] = client;
		clients[2] = client;
		
		JsonArray jsonClients = 
				JSerializer.json()
					.serialize(clients);
				
		System.out.println(jsonClients);
	}

}
