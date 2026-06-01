package com.WebfluxTest.Library.controller;

import com.WebfluxTest.Library.Repository.LibraryRepository;
import com.WebfluxTest.Library.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

@RestController
//Could be API versioning
@RequestMapping("/books")
    public class BookController {

    // Endpoint de acesso aos livros.
    private LibraryRepository repository;

    public BookController(LibraryRepository repository) {
        this.repository = repository;
    }

    //Endpoint que retorna os livros.
    @GetMapping
    public Flux<Book> getAllBooks() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Book>> getBookById(String id) {
        return repository.findById(id)
                .map(Book -> ResponseEntity.ok(Book))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}