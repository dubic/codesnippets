/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.controllers;

import com.dubic.codesnippets.dto.SnipData;
import com.dubic.codesnippets.spi.CodeSnippetsException;
import com.dubic.codesnippets.spi.SnippetService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${picture.location}")
    private String picturePath;

    @RequestMapping("/view")
    public String loadPage(@RequestParam("page") String page) {
        return "posts/" + page;
    }

    @RequestMapping(value = {"/img/{pic}"})
    public void processImage(HttpServletRequest request, HttpServletResponse response, @PathVariable("pic") String picId) {
        ServletOutputStream responseStream = null;
        try {
            response.setContentType("image/jpeg");
            responseStream = response.getOutputStream();

            if (!picId.equalsIgnoreCase("male")) {
                IOUtils.copy(new FileInputStream(picturePath + picId), responseStream);
            } else {
                //send avatar
                IOUtils.copy(new FileInputStream(picturePath + "male.jpg"), responseStream);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        } finally {
            try {
                responseStream.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(SnippetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    @RequestMapping("/snippet/view/{id}")
    public @ResponseBody
    JsonObject viewSnippet(@PathVariable("id") Long id,@RequestParam("edit") boolean isEdit) {
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

}
