package com.react.programinreact.test;

import com.react.programinreact.dto.Anime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class OperatiorsTest {

    @Test
    void subscritionsOnSimple() {

        var flux = Flux.range(1, 10)
                .map(integer -> {
                    System.out.println("Map 1: " + integer);
                    return integer;
                }).publishOn(Schedulers.boundedElastic())
                .map(integer -> {
                    System.out.println("Map 2: " + integer);
                    return integer;
                });

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .verifyComplete();

    }

    @Test
    void subscritionsOnSimple2() {

        var flux = Flux.range(1, 10)
                .map(integer -> {
                    System.out.println("Map 1: " + integer);
                    return integer;
                })
                .map(integer -> {
                    System.out.println("Map 2: " + integer);
                    return integer;
                }).subscribeOn(Schedulers.boundedElastic());

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .verifyComplete();
    }

    @Test
    void subscribeOnIO() {
        Mono<List<String>> list = Mono.fromCallable(() -> Files.readAllLines(Path.of("test-file")))
                .log()
                .subscribeOn(Schedulers.boundedElastic());

        list.subscribe(strings -> System.out.println(strings));

        StepVerifier.create(list)
                .expectSubscription()
                .thenConsumeWhile(strings -> {
                    Assertions.assertFalse(strings.isEmpty());
                    System.out.println("size: " + strings.size());
                    return true;
                }).verifyComplete();
    }

    @Test
    void switchIfEmptyOperator() {
        Flux<Object> flux = Flux.empty()
                .switchIfEmpty(Flux.just("not empty"));

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext("not empty")
                .expectComplete()
                .verify();


    }

    @Test
    void deferoperatorJust() throws InterruptedException {
        Flux<Object> flux = Flux.just(System.currentTimeMillis());

        flux.subscribe(o -> System.out.println(o));
        Thread.sleep(500);

        flux.subscribe(o -> System.out.println(o));
        Thread.sleep(500);

        flux.subscribe(o -> System.out.println(o));
        Thread.sleep(500);

        flux.subscribe(o -> System.out.println(o));
        Thread.sleep(500);
    }

    @Test
    void deferoperatorDefer() throws InterruptedException {
        Flux<Object> flux = Flux.defer(() -> Mono.just(System.currentTimeMillis()));

        flux.subscribe(o -> System.out.println(o));
        Thread.sleep(500);

        flux.subscribe(o -> System.out.println(o));
        Thread.sleep(500);

        flux.subscribe(o -> System.out.println(o));
        Thread.sleep(500);

        flux.subscribe(o -> System.out.println(o));
        Thread.sleep(500);
    }

    @Test
    void zipOperador() {
        Flux<String> titleFlux = Flux.just("Grand Blue", "Baki");
        Flux<String> studeo = Flux.just("Anime", "Disney");
        Flux<Integer> epsodios = Flux.just(1, 12);

        var zip = Flux.zip(titleFlux, studeo, epsodios);

        Flux<Anime> fluxAnime = Flux.zip(titleFlux, studeo, epsodios)
                .flatMap(objects -> Flux.just(new Anime(objects.getT1(), objects.getT2(), objects.getT3())));
    }

    ;
}

