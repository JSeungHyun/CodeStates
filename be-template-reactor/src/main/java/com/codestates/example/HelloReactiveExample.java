package com.codestates.example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class HelloReactiveExample{
    public static void main(String[] args) throws InterruptedException{
//        Mono
//                .just("Hello, Reactive")
//                .subscribe(message -> System.out.println(message));

        Flux // 여러 건의 데이터를 처리
                .just("Hello", "Reactor") // emit하는 Publisher의 역할
                .map(m -> m.toUpperCase())
                .publishOn(Schedulers.parallel())
                .subscribe(System.out::println,
                        error -> System.out.println(error.getMessage()),
                        () -> System.out.println("# onComplete"));
        Thread.sleep(100L);

    }
}
