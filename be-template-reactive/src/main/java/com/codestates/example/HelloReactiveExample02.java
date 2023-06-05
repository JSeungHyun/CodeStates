package com.codestates.example;

import reactor.core.publisher.Mono;

// 리액티브 프로그래밍 기본 구조
public class HelloReactiveExample02 {
    public static void main(String[] args) {
        Mono
                .just("Hello, Reactive")
                .subscribe(message -> System.out.println(message));
    }
}
