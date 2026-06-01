package com.WebfluxTest.Library;

import com.WebfluxTest.Library.Repository.LibraryRepository;
import com.WebfluxTest.Library.controller.BookController;
import com.WebfluxTest.Library.model.Book;
import com.WebfluxTest.Library.model.SequenceCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private LibraryRepository repository;

    @Mock
    private ReactiveMongoOperations operations;

    @InjectMocks
    private BookController bookController;

    private WebTestClient webTestClient;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(bookController).build();

        book1 = new Book("1", "Sleepless", "Charlie Houston", 4.5);
        book2 = new Book("2", "1984", "George Orwell", 5.0);
    }

    // GET /books — retorna todos os livros
    @Test
    void getAllBooks_deveRetornarTodosOsLivros() {
        when(repository.findAll()).thenReturn(Flux.just(book1, book2));

        webTestClient.get().uri("/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .hasSize(2);
    }

    // GET /books/{id} — livro encontrado
    @Test
    void getBookById_deveRetornarLivroQuandoEncontrado() {
        when(repository.findById("1")).thenReturn(Mono.just(book1));

        webTestClient.get().uri("/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(book1);
    }

    // GET /books/{id} — livro não encontrado
    @Test
    void getBookById_deveRetornar404QuandoNaoEncontrado() {
        when(repository.findById("99")).thenReturn(Mono.empty());

        webTestClient.get().uri("/books/99")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    // POST /books — cria um novo livro com ID sequencial
    @Test
    void createBook_deveCriarLivroComIdSequencial() {
        SequenceCounter counter = new SequenceCounter();
        counter.setSeq(1L);

        when(operations.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(SequenceCounter.class)))
                .thenReturn(Mono.just(counter));
        when(repository.save(any(Book.class))).thenReturn(Mono.just(book1));

        Book novoLivro = new Book(null, "Sleepless", "Charlie Houston", 4.5);

        webTestClient.post().uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(novoLivro)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .isEqualTo(book1);
    }

    // PUT /books — atualiza livro existente
    @Test
    void updateBook_deveAtualizarLivroExistente() {
        Book atualizado = new Book("1", "Sleepless Updated", "Charlie Houston", 4.8);

        when(repository.findById("1")).thenReturn(Mono.just(book1));
        when(repository.save(any(Book.class))).thenReturn(Mono.just(atualizado));

        webTestClient.put().uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(atualizado)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(atualizado);
    }

    // PUT /books — livro não encontrado
    @Test
    void updateBook_deveRetornar404QuandoLivroNaoExiste() {
        Book inexistente = new Book("99", "Inexistente", "Ninguem", 1.0);

        when(repository.findById("99")).thenReturn(Mono.empty());

        webTestClient.put().uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(inexistente)
                .exchange()
                .expectStatus().isNotFound();
    }

    // DELETE /books/{id} — deleta livro existente
    @Test
    void deleteBook_deveDeletarLivroExistente() {
        when(repository.findById("1")).thenReturn(Mono.just(book1));
        when(repository.delete(book1)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/books/1")
                .exchange()
                .expectStatus().isOk();
    }

    // DELETE /books/{id} — livro não encontrado
    @Test
    void deleteBook_deveRetornar404QuandoLivroNaoExiste() {
        when(repository.findById("99")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/books/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    // DELETE /books — deleta todos os livros
    @Test
    void emptybookDB_deveDeletarTodosOsLivros() {
        when(repository.deleteAll()).thenReturn(Mono.empty());

        webTestClient.delete().uri("/books")
                .exchange()
                .expectStatus().isOk();
    }
}
