/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.controllers;

import com.dubic.codesnippets.dto.ShareData;
import com.dubic.codesnippets.dto.SnipData;
import com.dubic.codesnippets.spi.CodeSnippetsException;
import com.dubic.codesnippets.spi.NotMeException;
import com.dubic.codesnippets.spi.SnippetService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * usr/share/apache-tomcat-7.0.54/webapps/codesnippets/WEB-INF/classes/com/dubic/codesnippets/controllers
 *
 * @author dubem
 */
@Controller
@RequestMapping("/snippets")
public class SnippetsController {

    private final Logger log = Logger.getLogger(getClass());
    @Autowired
    private SnippetService snippetService;

    @RequestMapping("/view")
    public String loadPage(@RequestParam("page") String page) {
        return "posts/" + page;
    }

    @RequestMapping("/view/open")
    public String loadUnsecurePage(@RequestParam("page") String page) {
        return "posts/" + page;
    }
//

    @RequestMapping("/save")
    public @ResponseBody
    JsonObject newSnippet(@RequestBody SnipData snipData) {
        JsonObject resp = new JsonObject();
        try {
            snippetService.createSnip(snipData);

            resp.addProperty("code", 0);
        } catch (PersistenceException ex) {
            log.fatal(ex);
            resp.addProperty("code", 500);
        } catch (CodeSnippetsException ex) {
            log.warn(ex.getMessage());
            resp.addProperty("code", 403);
            resp.addProperty("msg", ex.getMessage());
        } catch (Exception ex) {
            log.fatal(ex.getMessage(), ex);
            resp.addProperty("code", 500);
        }
        return resp;
    }

    @RequestMapping("/edit")
    public @ResponseBody
    JsonObject editSnippet(@RequestBody SnipData snipData) {
        JsonObject resp = new JsonObject();
        try {

            snippetService.editSnip(snipData);

            resp.addProperty("code", 0);
        } catch (PersistenceException ex) {
            log.fatal(ex);
            resp.addProperty("code", 500);
        } catch (CodeSnippetsException ex) {
            log.warn(ex.getMessage());
            resp.addProperty("code", 403);
            resp.addProperty("msg", ex.getMessage());
        } catch (Exception ex) {
            log.fatal(ex.getMessage(), ex);
            resp.addProperty("code", 500);
        }
        return resp;
    }

    @RequestMapping("/delete/{id}")
    public @ResponseBody
    JsonObject deleteSnippet(@PathVariable("id") Long id) {
        JsonObject resp = new JsonObject();
        try {

            snippetService.delete(id);
            resp.addProperty("code", 0);
        } catch (EntityNotFoundException ex) {
            log.error(ex);
            resp.addProperty("code", 500);
            resp.addProperty("msg", ex.getMessage());
        } catch (PersistenceException ex) {
            log.fatal(ex);
            resp.addProperty("code", 501);
            resp.addProperty("msg", "unxpected server error");
        } catch (NotMeException ex) {
            log.warn(ex.getMessage());
            resp.addProperty("code", 403);
            resp.addProperty("msg", ex.getMessage());
        } catch (Exception ex) {
            log.fatal(ex.getMessage(), ex);
            resp.addProperty("code", 501);
            resp.addProperty("msg", "unxpected server error");
        }
        return resp;
    }

    @RequestMapping("/load/latest")
    public @ResponseBody
    JsonArray loadLatests(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) {
        return snippetService.loadLatest(start, size);
    }

    @RequestMapping("/load/mine")
    public @ResponseBody
    JsonArray loadMine(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) {
        return snippetService.loadMine(start, size);
    }

    @RequestMapping("/load/shared")
    public @ResponseBody
    JsonArray loadShared(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) {
        return snippetService.loadShared(start, size);
    }

    @RequestMapping("/load/user/{id}")
    public @ResponseBody
    JsonArray loadUserSnippets(@PathVariable("id") Long id, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) {
        return snippetService.loadUserSnippets(id, start, size);
    }

    @RequestMapping("/snippet/view/{id}")
    public @ResponseBody
    JsonObject viewSnippet(@PathVariable("id") Long id, @RequestParam("edit") boolean isEdit) {
        try {
            JsonObject snippet = snippetService.getSnippet(id);
            if (!isEdit) {
                snippetService.updateViews(id);
            }
            return snippet;
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        return new JsonObject();
    }

    @RequestMapping("/share/{id}")
    public @ResponseBody
    JsonObject share(@PathVariable("id") Long id, @RequestBody ShareData shareData) {
        JsonObject resp = new JsonObject();
        try {
            List<String> errs = snippetService.share(id, shareData);
            resp.addProperty("code", 0);
            resp.add("errMsg", new Gson().toJsonTree(errs));
        } catch (NotMeException nme) {
            log.warn(nme.getMessage());
            resp.addProperty("code", 404);
            resp.addProperty("msg", nme.getMessage());
        } catch (CodeSnippetsException cse) {
            log.warn(cse.getMessage());
            resp.addProperty("code", 403);
            resp.addProperty("msg", cse.getMessage());
        }
        return resp;
    }

    @RequestMapping("/canedit/{id}")
    public @ResponseBody
    JsonObject canEdit(@PathVariable("id") Long id) {
        JsonObject resp = new JsonObject();
        try {
            boolean mine = snippetService.isMine(id);
            System.out.println("mine : " + mine);
            if (mine) {
                resp.addProperty("code", 0);
            } else {
                resp.addProperty("code", 404);
            }

        } catch (Exception e) {
            log.fatal(e.getMessage(), e);
            resp.addProperty("code", 500);
            resp.addProperty("msg", "Unexpected server error occurred");
        }
        return resp;
    }

}
