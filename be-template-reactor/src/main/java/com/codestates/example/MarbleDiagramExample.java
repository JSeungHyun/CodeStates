package com.codestates.example;

import reactor.core.publisher.Flux;

public class MarbleDiagramExample {
    public static void main(String[] args) {
        Flux
                .just("Green", "Orange", "Blue")
                .map(figure -> figure + " Rectangle")
                .subscribe(System.out::println);
    }
}
