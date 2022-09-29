package com.react.programinreact.test;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxTest {
    @Test
    void flushSubscriber() {
        List<String> strings = Arrays.asList("Thiago", "Henrique", "Fonseca", "Margonar");

        Flux<List<String>> flusxString = Flux.just(strings).log();

        StepVerifier.create(flusxString)
                .expectNext(strings)
                .verifyComplete();
    }

    @Test
    void flushSubscriberNumbers() {

        Flux<Integer> fluxInt = Flux.range(1, 5).log();

        fluxInt.subscribe(integer -> System.out.println(integer));

        StepVerifier.create(fluxInt)
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }
}
