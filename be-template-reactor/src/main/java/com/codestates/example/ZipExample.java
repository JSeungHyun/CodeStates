package com.codestates.example;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;

@Slf4j
public class ZipExample {
    public static void main(String[] args) throws InterruptedException {
        Flux<Long> source1 = Flux.interval(Duration.ofMillis(200L)).take(4);
        Flux<Long> source2 = Flux.interval(Duration.ofMillis(400L)).take(6);

        Flux
                .zip(source1, source2, (data1, data2) -> data1 + data2)
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(3000L);
    }
}
