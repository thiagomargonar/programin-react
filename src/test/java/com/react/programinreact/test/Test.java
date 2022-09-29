package com.react.programinreact.test;

import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Test {

    @org.junit.jupiter.api.Test
    void monoSubscriber() {
        String nome = "Thiago";
        var mono = Mono.just(nome).log();
        System.out.println("--------------");
        StepVerifier.create(mono)
                .expectNext("Thiago")
                .verifyComplete();

    }

    @org.junit.jupiter.api.Test
    void monoSubscriberConsumer() {
        String nome = "Thiago";
        var mono = Mono.just(nome).log();
        mono.subscribe(System.out::println);
        StepVerifier.create(mono)
                .expectNext("Thiago")
                .verifyComplete();

    }

    @org.junit.jupiter.api.Test
    void monoSubscriberConsumerError() {
        String nome = "Thiago";
        var mono = Mono.just(nome)
                        .map(s -> {
                            throw new RuntimeException("Testing mono with error");
                        });

        mono.subscribe(System.out::println, s-> System.out.println("Somethings bad happened"+s));

        mono.subscribe(System.out::println, Throwable::printStackTrace);


        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();

    }

    @org.junit.jupiter.api.Test
    void monoSubscriberConsumerComplete() {
        String nome = "Thiago";
        var mono = Mono.just(nome)
                        .map(String::toUpperCase);

        mono.subscribe(s -> System.out.println("value:"+s),
                Throwable::printStackTrace,
                () -> System.out.println("Finished"));

        StepVerifier.create(mono)
                .expectNext(nome.toUpperCase())
                .verifyComplete();

    }

    @org.junit.jupiter.api.Test
    void monoSubscriberConsumerSubscrition() {
        String nome = "Thiago";
        var mono = Mono.just(nome)
                .map(String::toUpperCase);

        mono.subscribe(s -> System.out.println("value:"+s),
                Throwable::printStackTrace,
                () -> System.out.println("Finished"),
                Subscription::cancel);

        mono.subscribe(s -> System.out.println("value:"+s),
                Throwable::printStackTrace,
                () -> System.out.println("Finished"),
                subscription -> subscription.request(1));


    }

    @org.junit.jupiter.api.Test
    void monoDoOnMethod() {
        String nome = "Thiago";
        var mono = Mono.just(nome)
                .map(String::toUpperCase)
                .doOnSubscribe(subscription -> System.out.println("doOnSubscribe: "+subscription))
                .doOnRequest(l -> System.out.println("doOnRequest: "+l))
                .doOnNext(s -> System.out.println("doOnNext: "+s))
                .doOnSuccess(s -> System.out.println("doOnSuccess: "+s));

        mono.subscribe(s -> System.out.println("value:"+s),
                Throwable::printStackTrace,
                () -> System.out.println("Finished"),
                Subscription::cancel);

        mono.subscribe(s -> System.out.println("value:"+s),
                Throwable::printStackTrace,
                () -> System.out.println("Finished"),
                subscription -> subscription.request(1));


    }
}
