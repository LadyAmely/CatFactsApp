package com.example.cat;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.time.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ApiController {

    private Map<String, Object> userData = new HashMap<>();
    private final Object lock = new Object();
    private final DataFetcher dataFetcher;

    ApiController(DataFetcher dataFetcher) {

        this.dataFetcher = dataFetcher;

    }

    @PostConstruct
    public void fetchingData(){

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(()->{

            while(true){
                try{

                    Map<String, Object> data = dataFetcher.fetchData();
                    synchronized (lock){
                        userData = data;
                    }

                    Thread.sleep(10000);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        });
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUserData(){
        synchronized (lock){
            return ResponseEntity.ok(userData);
        }
    }



}

@Component
class DataFetcher{
    private final WebClient webClient;

    public DataFetcher(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    public Map<String, Object> fetchData(){

        Mono<Map> response = webClient
                .get()
                .uri("https://randomuser.me/api/")
                .retrieve()
                .bodyToMono(Map.class);

        return response.block();
    }
}
