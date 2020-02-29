package com.bol.controller;

import org.springframework.web.bind.annotation.RestController;

import com.bol.entity.Play;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin
@RestController
@RequestMapping("/game")
public class GameController {

  @RequestMapping(value="/play", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public Play play(@RequestBody Play play) {
      return play;
  }
  
}