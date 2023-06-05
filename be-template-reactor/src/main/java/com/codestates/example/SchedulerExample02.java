package com.codestates.example;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * subscribeOn() Operator를 이용해서 Scheduler를 추가할 경우
 */
@Slf4j
public class SchedulerExample02 {
    public static void main(String[] args) throws InterruptedException {
        Flux
                .range(1, 10)
                .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
                .subscribeOn(Schedulers.boundedElastic())
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2)
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(100L);
    }
}