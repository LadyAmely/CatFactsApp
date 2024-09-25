package com.example.cat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /*
    @GetMapping(value = "/user/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UsernameResponse> streamUserData() {
        return Flux.interval(Duration.ofSeconds(10))
                .map(tick -> userService.getUsername())
                .filter(Objects::nonNull);
    }

     */

    /*
    @GetMapping(value = "/user/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> streamUserData() {
        return Flux.interval(Duration.ofSeconds(10))
                .map(tick -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("username", userService.getUsername());
                    response.put("text", userService.getText()); // Użyj metody getText() z userService
                    return response; // Zwróć mapę
                })
                .filter(map -> Objects.nonNull(map.get("username")) && Objects.nonNull(map.get("text"))); // Filtruj, aby upewnić się, że oba obiekty są dostępne
    }

     */


    @GetMapping(value = "/user/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> streamUserData() {
        return Flux.interval(Duration.ofSeconds(10))
                .map(tick -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("username", userService.getUsername());
                    response.put("text", userService.getText());
                    return response;
                })
                .filter(map -> Objects.nonNull(map.get("username")) && Objects.nonNull(map.get("text")));
    }









    @GetMapping("/data")
    public Map<String, Object> getData() throws Exception {
        // Utwórz obiekt URL
        URL url = new URL("https://cat-fact.herokuapp.com/facts/random"); // Upewnij się, że to jest poprawny URL
        // Otwórz połączenie
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        // Sprawdź kod odpowiedzi
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }

        // Odczytaj odpowiedź
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        // Użyj Jacksona do konwersji JSON na Map<String, Object>
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.toString(), new TypeReference<Map<String, Object>>() {});
    }
















}
