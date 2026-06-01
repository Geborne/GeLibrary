package com.WebfluxTest.Library;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

public class FluxTest {

    // Flux simples com 3 elementos
    @Test
    void flux_deveEmitirTresElementos() {
        Flux<String> flux = Flux.just("Book 1", "Book 2", "Book 3");

        StepVerifier.create(flux)
                .expectNext("Book 1")
                .expectNext("Book 2")
                .expectNext("Book 3")
                .verifyComplete();
    }

    // Flux a partir de uma lista
    @Test
    void flux_deveEmitirElementosDaLista() {
        List<String> livros = List.of("1984", "Duna", "Fundação");
        Flux<String> flux = Flux.fromIterable(livros);

        StepVerifier.create(flux)
                .expectNext("1984")
                .expectNext("Duna")
                .expectNext("Fundação")
                .verifyComplete();
    }

    // Flux com map — transforma cada elemento
    @Test
    void flux_deveTransformarElementosComMap() {
        Flux<String> flux = Flux.just("book 1", "book 2")
                .map(String::toUpperCase);

        StepVerifier.create(flux)
                .expectNext("BOOK 1")
                .expectNext("BOOK 2")
                .verifyComplete();
    }

    // Flux com filter — filtra elementos
    @Test
    void flux_deveFiltrarElementos() {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5)
                .filter(n -> n % 2 == 0); // só pares

        StepVerifier.create(flux)
                .expectNext(2)
                .expectNext(4)
                .verifyComplete();
    }

    // Flux com flatMap — transforma em outro Flux
    @Test
    void flux_deveFlatMapearElementos() {
        Flux<String> flux = Flux.just("A", "B")
                .flatMap(letra -> Flux.just(letra + "1", letra + "2"));

        StepVerifier.create(flux)
                .expectNextCount(4)
                .verifyComplete();
    }

    // Flux vazio
    @Test
    void flux_vazio_deveCompletarSemEmitir() {
        Flux<String> flux = Flux.empty();

        StepVerifier.create(flux)
                .verifyComplete();
    }

    // Flux com erro
    @Test
    void flux_deveEmitirErro() {
        Flux<String> flux = Flux.error(new RuntimeException("Erro no Flux"));

        StepVerifier.create(flux)
                .expectErrorMessage("Erro no Flux")
                .verify();
    }

    // Flux com take — limita a quantidade de elementos
    @Test
    void flux_deveEmitirApenasOsPrimeirosElementos() {
        Flux<Integer> flux = Flux.range(1, 100)
                .take(3);

        StepVerifier.create(flux)
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

    // Flux com interval — emite valores em intervalo de tempo
    @Test
    void flux_interval_deveEmitirComIntervalo() {
        Flux<Long> flux = Flux.interval(Duration.ofMillis(100))
                .take(3);

        StepVerifier.create(flux)
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }
}
