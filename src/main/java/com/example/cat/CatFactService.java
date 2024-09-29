package com.example.cat;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class CatFactService {

    private final WebClient webClient;
    private String username;
    private String text;

    CatFactService(){
        this.webClient = WebClient.create();
    }

    public Mono<Map> updateUserDataAsync() {
        return webClient.get()
                .uri("https://randomuser.me/api")
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(userData -> {
                    if (userData != null && userData.containsKey("results")) {
                        List<Map<String, Object>> results = (List<Map<String, Object>>) userData.get("results");
                        if (!results.isEmpty()) {
                            Map<String, Object> loginData = (Map<String, Object>) results.get(0).get("login");
                            this.username = (String) loginData.get("username");
                        }
                    }
                });
    }

    public Mono<UsernameResponse> getUsername() {
        return updateUserDataAsync()
                .flatMap(userData -> {
                    if (userData != null && userData.containsKey("results")) {
                        List<Map<String, Object>> results = (List<Map<String, Object>>) userData.get("results");
                        if (!results.isEmpty()) {
                            Map<String, Object> loginData = (Map<String, Object>) results.get(0).get("login");
                            String username = (String) loginData.get("username");
                            return Mono.just(new UsernameResponse(username));
                        }
                    }
                    return Mono.empty();
                });
    }

    public Mono<Map> getFactAsync(){
        return webClient.get()
                .uri("https://cat-fact.herokuapp.com/facts/random?source=user")
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(responseData -> {
                    if (responseData != null) {
                        text = (String) responseData.get("text");
                        System.out.println(text);
                    }
                });
    }

    public Mono<FactResponse> getText(){
        return getFactAsync()
                .flatMap(responseData->{
                    if(responseData != null){
                        String text = (String) responseData.get("text");
                        return Mono.just(new FactResponse(text));
                    }

                    return Mono.empty();
                });
    }

}
