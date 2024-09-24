package com.example.cat;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class UserService {


    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://randomuser.me/api";
    private Map<String, Object> userData;
    private String username;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UserService() {
        startUserDataUpdater();
    }


    private void startUserDataUpdater() {
        executorService.submit(() -> {
            while (true) {
                try {
                    updateUserData();
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }


    private void updateUserData() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
            userData = response.getBody();
            //System.out.println("Updated user data: " + userData);

            if (userData != null && userData.containsKey("results")) {

                List<Map<String, Object>> results = (List<Map<String, Object>>) userData.get("results");
                if (!results.isEmpty()) {

                    Map<String, Object> loginData = (Map<String, Object>) results.get(0).get("login");
                    username = (String) loginData.get("username");
                    System.out.println("Username: " + username);
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching user data: " + e.getMessage());
        }
    }


    public Map<String, Object> getUserData() {
        updateUserData();
        return userData;
    }

    public UsernameResponse getUsername() {
        updateUserData();
        return new UsernameResponse(username);
    }



    public void shutdown() {
        executorService.shutdownNow();
    }


}

