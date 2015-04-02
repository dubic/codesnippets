/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.spi;

import com.dubic.codesnippets.dto.SnipData;
import com.dubic.codesnippets.models.Snippet;
import com.dubic.codesnippets.models.User;
import com.dubic.codesnippets.util.IdmUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author dubem
 */
@Named
public class SnippetService {

    private final Logger log = Logger.getLogger(getClass());

    @Autowired
    private Database db;
    @Autowired
    private IdentityService identityService;

    public void createSnip(SnipData data) {
        log.debug(String.format("createSnip(%s)", new Gson().toJson(data)));
        Snippet snip = new Snippet(data);
        User user = identityService.getUserLoggedIn();
        if (user == null) {
            throw new CodeSnippetsException("you must be logged in to continue");
        }
        snip.setUser(user);
        db.persist(snip);
        log.info(String.format("New Snip [%s] by [%s]", snip.getTitle(), user.getScreenName()));
    }

    public JsonArray loadLatest(int start, int size) {
        log.debug(String.format("loadLatest(%d,%d)", start, size));
        String sql = "select s.id,s.create_dt,s.description,s.lang,s.title,u.screen_name,u.picture from snippets s,users u\n"
                + "where u.id=s.user_id ORDER by s.create_dt desc";
        return buildResults(sql,start,size);
    }
    
    public JsonArray loadMine(int start, int size) {
        log.debug(String.format("loadMine(%d,%d)", start, size));
        String sql = "select s.id,s.create_dt,s.description,s.lang,s.title,u.screen_name,u.picture from snippets s,users u\n"
                + "where u.id=s.user_id";
        return buildResults(sql,start,size);
    }


    public JsonObject getSnippet(Long id) throws IOException {

        String sql = "select s.id,s.create_dt,s.description,s.lang,s.title,u.screen_name,u.picture,s.code,s.dep from snippets s,users u\n"
                + "where u.id=s.user_id and s.id = " + id;
        List<Object[]> res = db.createNativeQuery(sql).getResultList();
        JsonObject jo = new JsonObject();
        for (Object[] o : res) {
            jo.addProperty("id", (Long) o[0]);
            jo.addProperty("create_dt", IdmUtils.formatDate((Date) o[1]));
            jo.addProperty("desc", (String) o[2]);
            jo.addProperty("lang", (String) o[3]);
            jo.addProperty("title", (String) o[4]);
            jo.addProperty("screenName", (String) o[5]);
            jo.addProperty("picture", (String) o[6]);
//            printCode((String) o[7]);
//            jo.add("code", new Gson().toJsonTree(lineCode((String) o[7])));
            jo.addProperty("code", (String) o[7]);
            String deps = (String) o[8];
            if (!IdmUtils.isEmpty(deps)) {
                jo.add("deps", new Gson().toJsonTree(deps.split(",")));
            }
        }
        return jo;
    }

    public void editSnip(SnipData snipData) {
        log.debug(String.format("editSnip(%s)", new Gson().toJson(snipData)));
//locate the snippet
        Snippet foundSnippet = db.find(Snippet.class, snipData.getId());
        if (!identityService.isMe(foundSnippet.getUser())) {
            throw new NotMeException("you cannot edit this snippet.it's not yours");
        }
        foundSnippet.setCode(snipData.getCode());
        foundSnippet.setDependecies(snipData.getDeps());
        foundSnippet.setDescription(snipData.getDesc());
        foundSnippet.setLang(snipData.getLang());
        foundSnippet.setModified(new Date());
        db.merge(foundSnippet);
        log.info(String.format("Snippet edited [%s] by [%s]", foundSnippet.getTitle(), foundSnippet.getUser().getScreenName()));
    }

    private void printCode(String code) {
        try {
            List<String> lines = IOUtils.readLines(IOUtils.toInputStream(code));
            for (int i = 0; i < lines.size(); i++) {
                System.out.println(i + ":" + lines.get(i));
//                if(i == 28)
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SnippetService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private List<String> lineCode(String code) throws IOException {
        return IOUtils.readLines(IOUtils.toInputStream(code));
    }

    public User getSnippetCreator(Long snipId) {
        return IdmUtils.getFirstOrNull(db.createQuery("SELECT s.user FROM Snippet s WHERE s.id = :id",User.class).setParameter("id", snipId).getResultList());
    }

    @Async
    public void updateViews(Long id) {
        Snippet find = db.find(Snippet.class, id);
        find.setViews(find.getViews()+1);
        db.merge(find);
        log.info("snippet views updated : "+id);
    }

    
    private JsonArray buildResults(String sql, int start, int size) {
        List<Object[]> res = db.createNativeQuery(sql).setFirstResult(start).setMaxResults(size).getResultList();
        JsonArray array = new JsonArray();
        for (Object[] o : res) {
            JsonObject jo = new JsonObject();
            jo.addProperty("id", (Long) o[0]);
            jo.addProperty("create_dt", IdmUtils.formatDate((Date) o[1]));
            jo.addProperty("desc", (String) o[2]);
            jo.addProperty("lang", (String) o[3]);
            jo.addProperty("title", (String) o[4]);
            jo.addProperty("screenName", (String) o[5]);
            jo.addProperty("picture", (String) o[6]);
            array.add(jo);
        }
        return array;
    }

}
