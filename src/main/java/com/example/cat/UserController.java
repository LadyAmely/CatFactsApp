package com.example.cat;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Objects;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/user/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UsernameResponse> streamUserData() {
        return Flux.interval(Duration.ofSeconds(10))
                .map(tick -> userService.getUsername())
                .filter(Objects::nonNull);
    }













}
