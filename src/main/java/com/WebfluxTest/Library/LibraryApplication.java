package com.WebfluxTest.Library;

import com.WebfluxTest.Library.Repository.LibraryRepository;
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
					new Book("1", "The Great Gatsby", "F. Scott Fitzgerald"),
					new Book("2", "To Kill a Mockingbird", "Harper Lee"),
					new Book("3", "1984", "George Orwell")
							.flatMap(repository::save));

			bookFlux
					.thenMany(repository.findAll())
					// Exibe os livros no console pensando em Reatividade
					.subscribe(System.out::println);
		};
	}
}
