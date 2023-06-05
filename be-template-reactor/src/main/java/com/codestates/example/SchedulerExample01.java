package com.codestates.example;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Scheduler를 추가하지 않을 경우
 */
@Slf4j
public class SchedulerExample01 {
    public static void main(String[] args) {
        Flux
                .range(1, 10)
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2)
                .subscribe(data -> log.info("# onNext: {}", data));
    }
}