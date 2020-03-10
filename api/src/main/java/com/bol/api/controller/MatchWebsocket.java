package com.bol.api.controller;

import com.bol.api.entity.InputMessage;
import com.bol.api.entity.OutputMessage;
import com.bol.api.service.MatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MatchWebsocket {

  @Autowired
  private MatchService matchService;

  @MessageMapping("/join")
  @SendTo("/match/join")
  public OutputMessage sendJoin(InputMessage message) {
    return matchService.send(message);
  }

  @MessageMapping("/play")
  @SendTo("/match/play")
  public OutputMessage sendPlay(InputMessage message) {
    return matchService.send(message);
  }

}