package com.react.programinreact.test;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

class hotPublisher {
    @Test
    void connectableFlux() throws InterruptedException {  //Independente de ter subscriber ele jera evento e voce consome a partir do connect
        ConnectableFlux connectableFlux = Flux.range(1,10)
                .delayElements(Duration.ofMillis(50))
                .publish();
        connectableFlux.connect();
//        Thread.sleep(100);
//        connectableFlux.subscribe(o -> System.out.println(o));
//
//        System.out.println("_---------------------");
//
//        System.out.println("Slpeeping");
//        Thread.sleep(100);
//
//        connectableFlux.subscribe(o -> System.out.println(o));
//
//        System.out.println("_---------------------");
//        System.out.println("Slpeeping");
//        Thread.sleep(200);

        connectableFlux.subscribe(o -> System.out.println(o));

        StepVerifier
                .create(connectableFlux)
                .then(connectableFlux::connect)
                .expectNext(1,2,3,4,5,6,7,8,9,10)
                .expectComplete()
                .verify();
    }
}
