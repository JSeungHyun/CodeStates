package com.codestates.coffee;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.utils.CustomBeanUtils;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

// TODO CoffeeService 에 Spring WebFlux 를 적용해 주세요. Spring MVC 방식 아닙니다!!
@Transactional
@Service
public class CoffeeService {
    private final CoffeeRepository coffeeRepository;
    private final CustomBeanUtils<Coffee> beanUtils;
    private final R2dbcEntityTemplate template;

    public CoffeeService(CoffeeRepository coffeeRepository, CustomBeanUtils<Coffee> beanUtils, R2dbcEntityTemplate template) {
        this.coffeeRepository = coffeeRepository;
        this.beanUtils = beanUtils;
        this.template = template;
    }

    public Mono<Coffee> createCoffee(Coffee coffee){
        return verifyExistCoffee(coffee.getCoffeeCode()).then(coffeeRepository.save(coffee));
    }

    public Mono<Coffee> updateCoffee(Coffee coffee){
        return findVerifiedCoffee(coffee.getCoffeeId())
                .map(c -> beanUtils.copyNonNullProperties(coffee, c))
                .flatMap(result -> coffeeRepository.save(result));
    }

    @Transactional(readOnly = true)
    public Mono<Coffee> findCoffee(long coffeeId){
        return findVerifiedCoffee(coffeeId);
    }

    public Mono<Void> verifyExistCoffee(String code) {
        return coffeeRepository.findByCoffeeCode(code)
                .flatMap(coffee -> {
                    if (code != null) {
                        return Mono.error(new BusinessLogicException(ExceptionCode.COFFEE_CODE_EXISTS));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Coffee> findVerifiedCoffee(long coffeeId){
        return coffeeRepository.findById(coffeeId)
                .switchIfEmpty(Mono.error(new BusinessLogicException(ExceptionCode.COFFEE_NOT_FOUND)));
    }
}
