/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.controllers;

import com.dubic.codesnippets.dto.UniqueValidation;
import com.dubic.codesnippets.dto.UserData;
import com.dubic.codesnippets.models.User;
import com.dubic.codesnippets.spi.CodeSnippetsException;
import com.dubic.codesnippets.spi.Database;
import com.dubic.codesnippets.spi.IdentityService;
import com.dubic.codesnippets.spi.SnippetService;
import com.google.gson.JsonObject;
import javax.persistence.EntityNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author dubem
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final Logger log = Logger.getLogger(getClass());
    @Autowired
    private IdentityService idmService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private Database db;
//    @Inject
//    private MailServiceImpl mailService;

    @RequestMapping("/load")
    public String loadPage(@RequestParam("p") String page) {
        return "users/" + page;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)//headers="Accept=application/json"
    public UserData screenNameExists() {
        System.out.println("SCREEN NAME EXISTS");
        return new UserData("dubic", "udubic@gmail.com", "dcamic");
    }

    @RequestMapping(value = "/email-unique")
    public @ResponseBody
    JsonObject emailIsValid(@RequestBody UniqueValidation validation) {
        JsonObject resp = new JsonObject();
        resp.addProperty("value", validation.getValue());
        resp.addProperty("isValid", idmService.getUniqueEmail(validation.getValue()) == null);
        return resp;
    }

    @RequestMapping(value = "/name-unique")
    public @ResponseBody
    JsonObject nameIsValid(@RequestBody UniqueValidation validation) {
        JsonObject resp = new JsonObject();
        resp.addProperty("value", validation.getValue());
        resp.addProperty("isValid", idmService.validateScreenName(validation.getValue()) == null);
        return resp;
    }

    @RequestMapping(value = "/current")
    public @ResponseBody
    JsonObject currentUser() {
        JsonObject resp = new JsonObject();
        User user = idmService.getUserLoggedIn();
        if (user == null) {
            resp.addProperty("code", 404);
            return resp;
        }
        resp.addProperty("code", 0);
        resp.addProperty("id", user.getId());
        resp.addProperty("email", user.getEmail());
        resp.addProperty("picture", user.getPicture());
        resp.addProperty("screenName", user.getScreenName());
        return resp;
    }

    @RequestMapping(value = "/isme/{snipId}")
    public @ResponseBody
    JsonObject isMe(@PathVariable("snipId") Long snipId) {
        JsonObject resp = new JsonObject();
        try {
            User creator = snippetService.getSnippetCreator(snipId);
            if (idmService.isMe(creator)) {
                resp.addProperty("code", 200);
                return resp;
            }
        } catch (CodeSnippetsException e) {
            resp.addProperty("code", 400);
            return resp;
        }
        resp.addProperty("code", 404);
        return resp;
    }

    @RequestMapping(value = "/user/{id}")
    public @ResponseBody
    JsonObject getUserById(@PathVariable("id") Long id) {
        JsonObject user = new JsonObject();
        try {
            User find = db.find(User.class, id);
            user.addProperty("email", find.getEmail());
            user.addProperty("username", find.getScreenName());
        } catch (EntityNotFoundException enfe) {
            log.warn("NO ENTITY HIT. PLS CHECK ID : " + id);
        }
        return user;
    }

}
