package com.bol.api.controller;

import com.bol.api.entity.Match;
import com.bol.api.service.MatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/match")
public class MatchController {

  @Autowired
  private MatchService matchService;

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Match getMatchById(@RequestParam String matchId) {
      return matchService.getMatchById(matchId);
  }

}