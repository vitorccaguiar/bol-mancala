package com.bol.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MatchController {

  // @Autowired
  // private MatchService matchService;

  @MessageMapping("/hello")
  @SendTo("/match/greetings")
  public String send(String message) {
    // return matchService.send(message);
    return "TEste";
  }

}