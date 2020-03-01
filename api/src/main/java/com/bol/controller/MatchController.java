package com.bol.controller;

import java.util.List;

import com.bol.entity.Match;
import com.bol.service.MatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/match")
public class MatchController {

  @Autowired
  private MatchService matchService;

  @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public Match createUser(@RequestBody Match match) {
    return matchService.saveMatch(match);
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Match> getAllMatches() {
    return matchService.getAllMatches();
  }
}