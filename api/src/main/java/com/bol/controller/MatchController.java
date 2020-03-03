package com.bol.controller;


import com.bol.entity.InputMessage;
import com.bol.entity.OutputMessage;
import com.bol.service.MatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MatchController {

  @Autowired
  private MatchService matchService;

  @MessageMapping("/match")
  @SendTo("/messages")
  public OutputMessage send(InputMessage message) {
    return matchService.send(message);
  }

}