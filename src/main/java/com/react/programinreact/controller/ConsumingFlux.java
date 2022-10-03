package com.react.programinreact.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/teste")
public class ConsumingFlux {

    @PostMapping
    public Flux<List<TestePess>> getAlgumaCoisa(@RequestBody TestePess s) {

        return Mono.just(s)
                .doOnNext(testePess -> System.out.println(testePess.getNome()))
                .filter(testePess -> testePess.getNome().equals("THIAGO"))
                .flatMapMany(this::getAcounts);
    }

    private Flux<List<TestePess>> getAcounts(TestePess testePess) {
        return Flux.just(Arrays.asList(new TestePess("234")));
    }

}
