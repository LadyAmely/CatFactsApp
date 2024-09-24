package com.example.cat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    @GetMapping("/user-service")
    public ResponseEntity<Map<String, Object>> getUserData() {
        Map<String, Object> userData = userService.getUserData();
        return ResponseEntity.ok(userData);
    }

     */

    /*

    @GetMapping(value = "/user/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> streamUserData() {

        return Flux.interval(Duration.ofSeconds(10))
                .map(tick -> userService.getUserData())
                .filter(data -> data != null);
    }

     */

    @GetMapping(value = "/user/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> streamUserData() {
        return Flux.interval(Duration.ofSeconds(10))
                .map(tick -> userService.getUserData())
                .filter(Objects::nonNull);
    }


}
