package com.WebfluxTest.Library;

import com.WebfluxTest.Library.Repository.LibraryRepository;
import com.WebfluxTest.Library.model.Book;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;


@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	// Inicializa banco de dados (MongoDB) com amostras de livro.
	@Bean
	CommandLineRunner init (ReactiveMongoOperations operations, LibraryRepository repository) {
		return args -> {
			Flux <com.WebfluxTest.Library.model.Book> bookFlux = Flux.just(
					new Book("1", "Sleepless", "Charlie Houston", 4.5),
					new Book("2", "To Kill a Mockingbird", "Harper Lee", 3.8),
					new Book("3", "1984", "George Orwell", 5.0),
					new Book("4", "If We Were Villains", "M.L. Rio", 5.0))
							.flatMap(repository::save);
			// Utilizando o mongoDB imbutido, pode-se invocar funções de forma limpa e reativa.
			bookFlux
					// Utiliza operações reativas para limpar a coleção e salvar os livros.
					.thenMany(repository.findAll())
					// Exibe os livros no console pensando em Reatividade
					.subscribe(System.out::println);
		};
	}
}
