package com.example.cat;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://randomuser.me/api";
    private Map<String, Object> userData;
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
            System.out.println("Updated user data: " + userData);
        } catch (Exception e) {
            System.err.println("Error fetching user data: " + e.getMessage());
        }
    }


    public Map<String, Object> getUserData() {
        return userData;
    }


    public void shutdown() {
        executorService.shutdownNow();
    }
}

