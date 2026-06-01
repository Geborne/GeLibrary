package com.WebfluxTest.Library.controller;

import com.WebfluxTest.Library.Repository.LibraryRepository;
import com.WebfluxTest.Library.model.Book;
import com.WebfluxTest.Library.model.BookEvent;
import com.WebfluxTest.Library.model.SequenceCounter;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

@RestController
//Could be API versioning
@RequestMapping("/books")
    public class BookController {

    // Endpoint de acesso aos livros.
    private LibraryRepository repository;
    private ReactiveMongoOperations operations;

    public BookController(LibraryRepository repository, ReactiveMongoOperations operations) {
        this.repository = repository;
        this.operations = operations;
    }

    //Endpoint que retorna os livros. Flux.
    @GetMapping
    public Flux<Book> getAllBooks() {
        return repository.findAll();
    }

    //Endpoint que filtra os livros por ID. Mono.
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Book>> getBookById(@PathVariable String id) {
        return repository.findById(id)
                .map(Book -> ResponseEntity.ok(Book))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //Endpoint que cria um novo livro. Mono.
    @PostMapping
    @ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)
    public Mono<Book> createBook(@RequestBody Book book) {
        // Garante que o ID seja gerado automaticamente.
        Query query = Query.query(Criteria.where("_id").is("books"));
        Update update = new Update().inc("seq", 1);

        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);

        return operations.findAndModify(query, update, options, SequenceCounter.class)
                .map(SequenceCounter::getSeq)
                .flatMap(id -> {
                    book.setID(String.valueOf(id));
                    return repository.save(book);
                }
                );
    }


    @PostMapping("/bulk")
    @ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)
    public Flux<Book> createBooks(@RequestBody Flux<Book> books) {
        return books.flatMap(book -> {
            Query query = Query.query(Criteria.where("_id").is("books"));
            Update update = new Update().inc("seq", 1);
            FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);

            return operations.findAndModify(query, update, options, SequenceCounter.class)
                    .map(SequenceCounter::getSeq)
                    .flatMap(id -> {
                        book.setID(String.valueOf(id));
                        return repository.save(book);
                    });
        });
    }


    //Endpoint que busca um livro por ID, e se existir, atualiza os campos com os dados fornecidos.
    @PutMapping
    @ResponseStatus(code = org.springframework.http.HttpStatus.OK)
    // Utiliza defaultIfEmpty para manter os valores existentes caso os campos sejam nulos ou vazios.
    public Mono<ResponseEntity<Book>> updateBook(@RequestBody Book book) {
        return repository.findById(book.getID())
                .flatMap(existingBook -> {
                    existingBook.setTitle(defaultIfEmpty(book.getTitle(), existingBook.getTitle()));
                    existingBook.setAuthor(defaultIfEmpty(book.getAuthor(), existingBook.getAuthor()));
                    existingBook.setRating(book.getRating() != null ? book.getRating() : existingBook.getRating());
                    return repository.save(existingBook);
                })
                .map(updatedBook -> ResponseEntity.ok(updatedBook))
                //Retorna not found caso o livro não for encontrado.
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //Endpoint que busca um livro por ID, e se existir, deleta o livro. Mono.
    @DeleteMapping("/{id}")
    // Utiliza flatMap para iniciar a operação de deleção e construção da resposta, garantindo que a resposta seja construída apenas após a deleção do livro.
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable String id) {
        return repository.findById(id)
                .flatMap(existingBook ->
                        //Passa o livro encontrado pelo ID e deleta.
                        repository.delete(existingBook)
                                //Retorna sucesso após a deleção. E
                                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());

        }

    //Deleta todos os livros da base. Mono
    @DeleteMapping
    public Mono<ResponseEntity<Void>> emptybookDB() {
        return repository.deleteAll()
                .then(Mono.just(ResponseEntity.ok().<Void>build()));
    }

    // Endpoint que simula um stream de eventos relacionados a livros. Flux.
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
            public Flux<BookEvent> getBookEvents() {
                return Flux.interval(Duration.ofSeconds(5))
                        .map(val -> new BookEvent(val, "Book Event - " + val));
            }

}