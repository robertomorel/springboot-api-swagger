package br.com.treinaweb.springbootapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Uma das conversões que o Spring Boot adota é que ele reconhece como componentes da
 * aplicação, todas as classes definidas no mesmo pacote da classe que contém o método
 * main ou em um package “abaixo” do package.
 * */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
