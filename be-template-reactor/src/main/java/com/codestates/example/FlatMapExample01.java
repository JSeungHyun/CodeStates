package com.codestates.example;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class FlatMapExample01 {
    public static void main(String[] args) throws InterruptedException {
        Flux
                .range(2, 6)         // (1)
                .flatMap(dan -> Flux
                        .range(1, 9)  // (2)
                        .publishOn(Schedulers.parallel())   // (3)
                        .map(num -> dan + " x " + num + " = " + dan * num)) // (4)
                .subscribe(log::info);

        Thread.sleep(100L);
    }
}