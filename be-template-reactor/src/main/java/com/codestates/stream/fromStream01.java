package com.codestates.stream;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

@Slf4j
public class fromStream01 {
    public static void main(String[] args) {
        // fromStream
        Flux
                .fromStream(Stream.of(200, 300, 400, 500, 600))
                .reduce((a, b) -> a + b)
                .subscribe(System.out::println);

        // fromIterable
        Flux
                .fromIterable(SampleData.coffeeList)
                .subscribe(coffee -> log.info("{} : {}", coffee.getKorName(), coffee.getPrice()));
    }
}
