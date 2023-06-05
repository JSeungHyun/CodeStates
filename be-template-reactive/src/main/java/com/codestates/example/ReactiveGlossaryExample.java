package com.codestates.example;

import reactor.core.publisher.Flux;

import java.util.List;

public class ReactiveGlossaryExample {
    public static void main(String[] args) {
        Flux // Publisher : 데이터를 내보내는 주체
                .fromIterable(List.of(1, 3, 6, 7, 8, 11))
                .filter(number -> number > 4 && (number % 2 == 0))
                .reduce((n1, n2) -> n1 + n2)
                .subscribe(System.out::println);

    }
}