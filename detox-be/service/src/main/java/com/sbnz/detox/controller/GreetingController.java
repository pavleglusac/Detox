package com.sbnz.detox.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo({"/topic/greetings", "/queue/reply"})
    public String greeting(String message) {
        System.out.println("Stigla ti je poooruka " + message);
        return "Hello, " + message + "!";
    }

}
