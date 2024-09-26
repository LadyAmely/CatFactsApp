package com.example.cat;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.Objects;

@RestController
@CrossOrigin("*")
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



    /*

    @GetMapping(value = "/cat-facts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UserTextResponse> streamUserData() {
        return Flux.interval(Duration.ofSeconds(10))
                .map(tick -> {

                    FactResponse factResponse = userService.getText();
                    UsernameResponse usernameResponse = userService.getUsername();


                    String text = factResponse.getText();
                    String username = usernameResponse.getUsername();


                    return new UserTextResponse(username, text);
                })
                .filter(userTextResponse ->
                        Objects.nonNull(userTextResponse.getUsername()) &&
                                Objects.nonNull(userTextResponse.getText())
                );
    }

     */

}
