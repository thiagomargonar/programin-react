package com.react.programinreact.test;


import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class MonoTesting {

    @Test
    void monoSubscriber() {
        String nome = "Thiago";
        var mono = Mono.just(nome);
        System.out.println("--------------");
        StepVerifier.create(mono)
                .expectNext("Thiago")
                .verifyComplete();

    }

    @Test
    void monoSubscriberConsumer() {
        String nome = "Thiago";
        var mono = Mono.just(nome).log();
        mono.subscribe(System.out::println);
        StepVerifier.create(mono)
                .expectNext("Thiago")
                .verifyComplete();

    }

    @Test
    void monoSubscriberConsumerError() {
        String nome = "Thiago";
        var mono = Mono.just(nome)
                .map(s -> {
                    throw new RuntimeException("Testing mono with error");
                });

        mono.subscribe(System.out::println, s -> System.out.println("Somethings bad happened" + s));

        mono.subscribe(System.out::println, Throwable::printStackTrace);


        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();

    }

    @Test
    void monoSubscriberConsumerComplete() {
        String nome = "Thiago";
        var mono = Mono.just(nome)
                .map(String::toUpperCase);

        mono.subscribe(s -> System.out.println("value:" + s),
                Throwable::printStackTrace,
                () -> System.out.println("Finished"));

        StepVerifier.create(mono)
                .expectNext(nome.toUpperCase())
                .verifyComplete();

    }

    @Test
    void monoSubscriberConsumerSubscrition() {
        String nome = "Thiago";
        var mono = Mono.just(nome)
                .map(String::toUpperCase);

        mono.subscribe(s -> System.out.println("value:" + s),
                Throwable::printStackTrace,
                () -> System.out.println("Finished"),
                Subscription::cancel);

        mono.subscribe(s -> System.out.println("value:" + s),
                Throwable::printStackTrace,
                () -> System.out.println("Finished"),
                subscription -> subscription.request(1));
    }

    @Test
    void monoDoOnMethod() {
        String nome = "Thiago";
        Mono<Object> mono = Mono.just(nome)
                .log()
                .map(String::toUpperCase)
                .doOnSubscribe(subscription -> System.out.println("doOnSubscriber"))
                .doOnRequest(l -> System.out.println("doOnRequest: " + l))
                .doOnNext(s -> System.out.println("doOnNext: " + s))
                .flatMap(s -> Mono.empty())
                .doOnNext(o -> System.out.println("doOneNext: " + o))
                .doOnSuccess(o -> System.out.println("sucess: " + o));

        mono.subscribe(s -> System.out.println("value:" + s),
                Throwable::printStackTrace,
                () -> System.out.println("Finished"),
                subscription -> subscription.request(1));
    }

    @Test
    void monoDoOnError() {
        Mono mono = Mono.error(new IllegalArgumentException())
                .doOnError(t -> System.out.println("Erro aqui: " + t))
                .doOnNext(o -> System.out.println("Não vai executar por que doOnError fecha a execução"))
                .log();

        StepVerifier.create(mono)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void monoDoOnErrorResume() {
        Mono mono = Mono.error(new IllegalArgumentException())
                .onErrorResume(throwable -> {
                    System.out.println("Comportamento após o erro");
                    return Mono.just("Novo conteudo");
                })
                .doOnNext(o -> System.out.println("Agora executa por que o erro acima foi tratado"))
                .log();

        mono.subscribe(o -> System.out.println(o));

        StepVerifier.create(mono)
                .expectNext("Novo conteudo")
                .verifyComplete();
    }
}
