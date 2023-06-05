package com.codestates.coffee;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CoffeeRepository extends R2dbcRepository<Coffee, Long> {

    Mono<Coffee> findByCoffeeCode(String code);
    Flux<Coffee> findAllBy(Pageable pageable);
}
