package com.example.cat;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

@RestController
@CrossOrigin("*")
public class CatFactController {

    private final CatFactService catFactService;

    public CatFactController(CatFactService catFactService) {
        this.catFactService = catFactService;
    }

    @GetMapping(value = "/cat-facts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<FullResponse> streamUserData() {
        return Flux.interval(Duration.ofSeconds(10))
                .flatMap(tick ->
                        Mono.zip(catFactService.getText(), catFactService.getUsername())
                                .map(tuple -> new FullResponse(
                                        tuple.getT2().getUsername(),
                                        tuple.getT1().getText()
                                ))
                )
                .doOnError(error -> System.err.println("Error fetching user data: " + error.getMessage()));
    }
}
