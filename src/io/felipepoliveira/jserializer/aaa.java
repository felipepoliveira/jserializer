package io.felipepoliveira.jserializer;

public class aaa {
	
public static void main(String[] args) {
	System.out.println(JSerializer.json().parseJfo("{\"require\" :  [\"itens\"]}").getFilteredFields());
}

}
