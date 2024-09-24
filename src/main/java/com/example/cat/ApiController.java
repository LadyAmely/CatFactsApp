package com.example.cat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class ApiController {

    private final WebClient webClient;

    ApiController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/api/external-data")
    Mono<Map> getExternalData() {

        String externalApiUrl = "https://cat-fact.herokuapp.com/facts/random";
        String userApiUrl ="https://randomuser.me/api/";


        return webClient.get()
                .uri(userApiUrl)
                .retrieve()
                .bodyToMono(Map.class);
    }



}
