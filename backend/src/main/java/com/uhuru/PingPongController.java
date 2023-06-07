package com.uhuru;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    record PingPong(String results){}

    @GetMapping("/ping")
    public PingPong getPingPong(){
        return new PingPong("Pong");
    }

}
