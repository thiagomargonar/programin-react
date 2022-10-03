package com.react.programinreact.test;


import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

class FluxTest {
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

    @Test
    void flushSubscriberFromList() {

        Flux<Integer> fluxInt = Flux.fromIterable(List.of(1, 2, 3, 4))
                .log();

        fluxInt.subscribe(integer -> System.out.println(integer));

        StepVerifier.create(fluxInt)
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    void flushSubscriberFromErros() {

        Flux<Integer> fluxInt = Flux.range(1, 10)
                .map(integer -> {
                    if (integer == 4) {
                        throw new IndexOutOfBoundsException();
                    }
                    return integer;
                });

        StepVerifier.create(fluxInt)
                .expectNext(1, 2, 3)
                .expectError(IndexOutOfBoundsException.class)
                .verify();
    }

    @Test
    void flushSubscriberFromUglyBlackPressure() {

        Flux<Integer> flux = Flux.range(1, 10).log();

        flux.subscribe(new Subscriber<Integer>() {
            private Subscription subscription;
            private int requestCount = 0;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(2);
            }

            @Override
            public void onNext(Integer integer) {
                requestCount++;
                if (requestCount >= 2) {
                    requestCount = 0;
                    subscription.request(2);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });


        StepVerifier.create(flux)
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .verifyComplete();
    }

    @Test
    void flushSubscriberPrettyBackPressure() {

        Flux<Integer> fluxInt = Flux.range(1, 10)
                .log()
                .limitRate(3);

        fluxInt.subscribe(integer -> System.out.println(integer));

        StepVerifier.create(fluxInt)
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .verifyComplete();
    }

    @Test
    void flushSubscriberFromNotSoUglyBlackPressure() {

        Flux<Integer> flux = Flux.range(1, 10).log();
        flux.subscribe(new BaseSubscriber<Integer>() {

            int requestCount = 2;
            int count = 0;

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(requestCount);
            }

            @Override
            protected void hookOnNext(Integer value) {
                count++;
                if (count >= requestCount) {
                    count = 0;
                    request(requestCount);
                }
            }
        });


        StepVerifier.create(flux)
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .verifyComplete();
    }

    @Test
    void flushSubscriberIntevalOne() throws InterruptedException {
        var interval = createInterval();
        interval.subscribe(aLong -> System.out.println(interval));
    }

    @Test
    void flushSubscriberIntevalTwo() throws InterruptedException {
        StepVerifier.withVirtualTime(() -> createInterval())//
                .expectSubscription()
                .thenAwait(Duration.ofDays(5)) //em 5 dias nada será publicado
                .expectNoEvent(Duration.ofDays(10))
                .expectNext(0L)
                .expectNext(1L)
                .thenCancel()
                .verify();
    }

    private static Flux<Long> createInterval() {
        return Flux.interval(Duration.ofMillis(100))
                .take(10)
                .log();
    }
}
