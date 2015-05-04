/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.controllers;

import com.dubic.codesnippets.spi.Database;
import com.dubic.codesnippets.spi.IdentityService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Dubic
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    private final Logger log = Logger.getLogger(getClass());
    @Autowired
    private IdentityService idmService;
    @Autowired
    private Database db;

    @RequestMapping("/results/snippets")
    public @ResponseBody
    JsonObject searchSnippets(
            @RequestParam("q") String keyword,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        JsonObject resp = new JsonObject();
        //search snippets
        String snipsql = "select s.title,s.description,s.id from snippets s where s.title like '%" + keyword + "%'";
        List<Object[]> searchSnippets = db.createNativeQuery(snipsql).setFirstResult(start).setMaxResults(size).getResultList();
        JsonArray snips = new JsonArray();
        for (Object[] snippet : searchSnippets) {
            JsonObject s = new JsonObject();
            s.addProperty("title", (String) snippet[0]);
            s.addProperty("desc", (String) snippet[1]);
            s.addProperty("id", (Long) snippet[2]);
            snips.add(s);
        }
        resp.add("snippets", snips);
        //count
        String snipcountsql = "select count(s.id) from snippets s where s.title like '%" + keyword + "%'";
        resp.addProperty("snipcount", (Long) db.createNativeQuery(snipcountsql).getSingleResult());
        return resp;
    }

    @RequestMapping("/results/users")
    public @ResponseBody
    JsonObject searchUsers(
            @RequestParam("q") String keyword,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        JsonObject resp = new JsonObject();
        //search snippets
        String usersql = "select u.id,u.screen_name,u.firstname,u.lastname from users u where\n"
                + " u.screen_name like '%"+keyword+"%' or u.firstname like '%"+keyword+"%' or u.lastname like '%"+keyword+"%'";
        List<Object[]> users = db.createNativeQuery(usersql).setFirstResult(start).setMaxResults(size).getResultList();
        JsonArray uarray = new JsonArray();
        for (Object[] user : users) {
            JsonObject s = new JsonObject();
            s.addProperty("id", (Long) user[0]);
            s.addProperty("username", (String) user[1]);
            s.addProperty("firstname", (String) user[2]);
            s.addProperty("lastname", (String) user[3]);
            uarray.add(s);
        }
        resp.add("users", uarray);
        //count
        String usercountsql = "select count(u.id) from users u where\n"
                + " u.screen_name like '%"+keyword+"%' or u.firstname like '%"+keyword+"%' or u.lastname like '%"+keyword+"%'";
        resp.addProperty("userscount", (Long) db.createNativeQuery(usercountsql).getSingleResult());
        return resp;
    }
}
