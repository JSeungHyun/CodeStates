package com.codestates.coffee;

import com.codestates.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

// TODO CoffeeController 에 Spring WebFlux 를 적용해 주세요. Spring MVC 방식 아닙니다!!
@RestController
@RequestMapping("/v12/coffees")
public class CoffeeController {
    private final CoffeeService coffeeService;
    private final CoffeeMapper mapper;

    public CoffeeController(CoffeeService coffeeService, CoffeeMapper mapper) {
        this.coffeeService = coffeeService;
        this.mapper = mapper;
    }

    @PostMapping
    public Mono<ResponseEntity> postCoffee(@Valid @RequestBody Mono<CoffeeDto.Post> requestBody){
        return requestBody
                .flatMap(post -> coffeeService.createCoffee(mapper.coffeePostDtoToCoffee(post)))
                .map(result -> {
                    URI location = UriCreator.createUri("/v12/coffees", result.getCoffeeId());
                    return ResponseEntity.created(location).build();
                });
    }

    @PatchMapping("/{coffee-id}")
    public ResponseEntity patchCoffee(@PathVariable("coffee-id") @Positive long coffeeId,
                                      @Valid @RequestBody Mono<CoffeeDto.Patch> requestBody){
        Mono<CoffeeDto.Response> response = requestBody
                .flatMap(patch -> {
                    patch.setCoffeeId(coffeeId);
                    return coffeeService.updateCoffee(mapper.coffeePatchDtoToCoffee(patch));
                })
                .map(coffee -> mapper.coffeeToCoffeeResponseDto(coffee));

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/{coffee-id}")
    public ResponseEntity getCoffee(@PathVariable("coffee-id") @Positive long coffeeId){
        Mono<CoffeeDto.Response> response =
                coffeeService.findCoffee(coffeeId)
                        .map(coffee -> mapper.coffeeToCoffeeResponseDto(coffee));
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
