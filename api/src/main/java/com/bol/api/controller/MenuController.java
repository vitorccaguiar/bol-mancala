package com.bol.api.controller;

import java.util.List;

import com.bol.api.entity.InputMessage;
import com.bol.api.entity.Match;
import com.bol.api.service.MenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value="new", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Match createMatch(@RequestBody InputMessage message) {
        return menuService.createMatch(message.getPlayerId(), message.getFingerprint());
    }

    @RequestMapping(value="match", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Match> getAllMatches() {
        return menuService.getAllMatches();
    }

}