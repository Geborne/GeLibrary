package com.WebfluxTest.Library;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {

    // Mono simples com 1 elemento
    @Test
    void mono_deveEmitirUmElemento() {
        Mono<String> mono = Mono.just("Book 1");

        StepVerifier.create(mono)
                .expectNext("Book 1")
                .verifyComplete();
    }

    // Mono vazio — como um Optional.empty()
    @Test
    void mono_vazio_deveCompletarSemEmitir() {
        Mono<String> mono = Mono.empty();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    // Mono com erro
    @Test
    void mono_deveEmitirErro() {
        Mono<String> mono = Mono.error(new RuntimeException("Erro no Mono"));

        StepVerifier.create(mono)
                .expectErrorMessage("Erro no Mono")
                .verify();
    }

    // Mono com map — transforma o valor
    @Test
    void mono_deveTransformarValorComMap() {
        Mono<String> mono = Mono.just("book 1")
                .map(String::toUpperCase);

        StepVerifier.create(mono)
                .expectNext("BOOK 1")
                .verifyComplete();
    }

    // Mono com flatMap — transforma em outro Mono
    @Test
    void mono_deveFlatMapearParaOutroMono() {
        Mono<String> mono = Mono.just("Book")
                .flatMap(titulo -> Mono.just(titulo + " - Encontrado"));

        StepVerifier.create(mono)
                .expectNext("Book - Encontrado")
                .verifyComplete();
    }

    // Mono com defaultIfEmpty — valor padrão quando vazio
    @Test
    void mono_deveRetornarValorPadraoQuandoVazio() {
        Mono<String> empty = Mono.empty();
        Mono<String> mono = empty.defaultIfEmpty("Livro não encontrado");

        StepVerifier.create(mono)
                .expectNext("Livro não encontrado")
                .verifyComplete();
    }

    // Mono com filter — vira vazio se não passar no filtro
    @Test
    void mono_deveVirarVazioSeNaoPassarNoFiltro() {
        Mono<Integer> mono = Mono.just(3)
                .filter(n -> n % 2 == 0); // 3 não é par, vira empty

        StepVerifier.create(mono)
                .verifyComplete();
    }

    // Mono com onErrorReturn — retorna valor padrão em caso de erro
    @Test
    void mono_deveRetornarValorPadraoEmCasoDeErro() {
        Mono<String> error = Mono.error(new RuntimeException("Falhou"));
        Mono<String> mono = error.onErrorReturn("Valor de fallback");

        StepVerifier.create(mono)
                .expectNext("Valor de fallback")
                .verifyComplete();
    }

    // Mono com zipWith — combina dois Monos
    @Test
    void mono_deveCombinarDoisMonos() {
        Mono<String> titulo = Mono.just("1984");
        Mono<String> autor = Mono.just("George Orwell");

        Mono<String> combinado = titulo.zipWith(autor, (t, a) -> t + " - " + a);

        StepVerifier.create(combinado)
                .expectNext("1984 - George Orwell")
                .verifyComplete();
    }
}
