package com.bol.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.bol.entity.Match;
import com.bol.service.MatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/match")
public class MatchController {
  HashMap<String, String> playerMatch = new HashMap<>();
  HashMap<String, String> playerMachine = new HashMap<>();

  @Autowired
  private MatchService matchService;

  @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public Match saveMatch(@RequestBody String playerId, @RequestBody String fingerprint) {
    Match returnedMatch = matchService.saveMatch(playerId);
    playerMatch.put(playerId, returnedMatch.getId());
    playerMachine.put(playerId, fingerprint);
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Match> getAllMatches() {
    return matchService.getAllMatches();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<Match> getMatchById(@PathVariable String id) {
    return matchService.getMatchById(id);
  }
}