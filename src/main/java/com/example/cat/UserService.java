package com.example.cat;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Service
public class UserService {


    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://randomuser.me/api";
    private final String factUrl = "https://cat-fact.herokuapp.com/facts/random?source=user";
    //private final String factUrl = "https://cat-fact.herokuapp.com/facts?animal_type=cat&category=history";
    private Map<String, Object> userData;
    private String username;
    private String text;
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


            if (userData != null && userData.containsKey("results")) {

                List<Map<String, Object>> results = (List<Map<String, Object>>) userData.get("results");
                if (!results.isEmpty()) {

                    Map<String, Object> loginData = (Map<String, Object>) results.get(0).get("login");
                    username = (String) loginData.get("username");
                    System.out.println("User: " + username);
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching user data: " + e.getMessage());
        }
    }


    private void fetchTextData() {
        try {

            ResponseEntity<Map> response = restTemplate.getForEntity(factUrl, Map.class);
            Map<String, Object> responseData = response.getBody();

            if (responseData != null) {

                 text = (String) responseData.get("text");
                System.out.println(text);
            }

        } catch (Exception e) {
            System.err.println("Error fetching text data: " + e.getMessage());
        }
    }


    public Map<String, Object> getUserData() {
        updateUserData();
        return userData;
    }

    public FactResponse getText(){
        fetchTextData();
        /*
        if (filterText(text)) {
            return new FactResponse(text);
        }

         */

        return new FactResponse(text);
    }

    public UsernameResponse getUsername() {
        updateUserData();
        return new UsernameResponse(username);
    }

    public boolean filterText(String text) {

        return text != null && text.toLowerCase().contains("cats");
    }



    public void shutdown() {
        executorService.shutdownNow();
    }


}

