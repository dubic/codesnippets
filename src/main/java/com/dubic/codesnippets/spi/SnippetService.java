/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.spi;

import com.dubic.codesnippets.dto.Share;
import com.dubic.codesnippets.dto.ShareData;
import com.dubic.codesnippets.dto.SnipData;
import com.dubic.codesnippets.models.Notification;
import com.dubic.codesnippets.models.Shared;
import com.dubic.codesnippets.models.Snippet;
import com.dubic.codesnippets.models.User;
import com.dubic.codesnippets.util.IdmUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private NotificationService notifService;
    @Value("${shared.msg}")
    private String shareMsg;

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
                + "where u.id=s.user_id and u.id=" + identityService.getUserLoggedIn().getId() + ""
                + " ORDER by s.create_dt desc";
        return buildResults(sql, start, size);
    }

    public JsonArray loadMine(int start, int size) {
        log.debug(String.format("loadMine(%d,%d)", start, size));
        String sql = "select s.id,s.create_dt,s.description,s.lang,s.title,u.screen_name,u.picture from snippets s,users u\n"
                + "where u.id=s.user_id and u.id=" + identityService.getUserLoggedIn().getId();
        return buildResults(sql, start, size);
    }

    public JsonArray loadUserSnippets(Long userId, int start, int size) {
        log.debug(String.format("loadUserSnippets(%d,%d,%d)", userId, start, size));
        String sql = "select s.id,s.create_dt,s.description,s.lang,s.title,u.screen_name,u.picture from snippets s,users u\n"
                + "where u.id=s.user_id and u.id=" + userId;
        return buildResults(sql, start, size);
    }

    public JsonArray loadShared(int start, int size) {
        log.debug(String.format("loadShared(%d,%d)", start, size));
        String sql = "select s.id,s.title,s.lang from snippets s,shared sh where sh.snippet_id=s.id and sh.to_user_id=" + identityService.getUserLoggedIn().getId();
        List<Object[]> res = db.createNativeQuery(sql).setFirstResult(start).setMaxResults(size).getResultList();
        JsonArray array = new JsonArray();
        for (Object[] o : res) {
            JsonObject jo = new JsonObject();
            jo.addProperty("id", (Long) o[0]);
//            jo.addProperty("create_dt", IdmUtils.formatDate((Date) o[1]));
//            jo.addProperty("desc", (String) o[2]);
            jo.addProperty("lang", (String) o[2]);
            jo.addProperty("title", (String) o[1]);
//            jo.addProperty("screenName", (String) o[5]);
//            jo.addProperty("picture", (String) o[6]);
            array.add(jo);
        }
        return array;
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
//            java.util.logging.LogManager.getLogger(SnippetService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private List<String> lineCode(String code) throws IOException {
        return IOUtils.readLines(IOUtils.toInputStream(code));
    }

    public User getSnippetCreator(Long snipId) {
        return IdmUtils.getFirstOrNull(db.createQuery("SELECT s.user FROM Snippet s WHERE s.id = :id", User.class).setParameter("id", snipId).getResultList());
    }

    @Async
    public void updateViews(Long id) {
        Snippet find = db.find(Snippet.class, id);
        find.setViews(find.getViews() + 1);
        db.merge(find);
        log.info("snippet views updated : " + id);
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

    public List<String> share(Long id, ShareData shareData) throws CodeSnippetsException, NotMeException {
        log.debug(String.format("share(%s)", new Gson().toJson(shareData)));
        //return a list of messages
        List<String> errs = new ArrayList<String>();
        //must be in session
        Snippet foundSnippet = db.find(Snippet.class, id);
        if (foundSnippet == null) {
            throw new CodeSnippetsException("The Snippet you selected no longer exists");
        }
        User me = identityService.getUserLoggedIn();
        if (!me.equals(foundSnippet.getUser())) {
            //user logged in must be the owner of the snippet
            throw new NotMeException("you cannot share this snippet.it's not yours");
        }
        for (Share s : shareData.getSharelist()) {
            try {
                Shared share = new Shared();
                share.setSnippet(foundSnippet);
                share.setBy(me);
                share.setMsg(shareData.getMsg());
                if (s.isUser()) {
                    User to = identityService.findUserByScreenName(s.getName());
                    share.setTo(to);
                    //snippet has been shared previously
                    if (isShared(id, to.getId())) {
                        errs.add("This snippet has already been shared with " + to.getScreenName());
                        continue;
                    }
                    //snippet is being shared with it's owner
                    if (foundSnippet.getUser().equals(to)) {
                        errs.add("This snippet cannot be shared with it's owner : " + to.getScreenName());
                        continue;
                    }
                    db.persist(share);
                    //SEND MAIL TO USER
                    String msg = String.format(shareMsg, me.getScreenName(), foundSnippet.getTitle());
                    notifService.notify(notifService.getSettings(to), msg, Notification.Type.SHARE, foundSnippet.getId().toString());
                } else {
                    //SEND MAIL TO USER
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                errs.add("Unexpected error. Could not share snippet with " + s.getName());
            }
        }
        return errs;
    }

    /**
     * checks if this snippet has been shared with the user
     *
     * @param snippet the snippet id
     * @param user the user id that the snippet was shared with
     * @return true if a record with both ids exists
     */
    public boolean isShared(Long snippet, Long user) {
        return !db.createQuery("SELECT s.id FROM Shared s WHERE s.snippet.id = :sid AND s.to.id = :uid")
                .setParameter("sid", snippet).setParameter("uid", user).getResultList().isEmpty();

    }

    public boolean isMine(Long id) {
        return !db.createQuery("SELECT s.id FROM Snippet s WHERE s.id = :sid AND s.user.id = :uid")
                .setParameter("sid", id).setParameter("uid", identityService.getUserLoggedIn().getId()).getResultList().isEmpty();
    }

    @Transactional
    public void delete(Long id) {
        log.debug(String.format("delete(%d)", id));
//locate the snippet
        Snippet foundSnippet = db.find(Snippet.class, id);
        if (foundSnippet == null) {
            throw new EntityNotFoundException("snippet with id : " + id + " not found");
        }
        if (!identityService.isMe(foundSnippet.getUser())) {
            throw new NotMeException("you cannot delete this snippet.it's not yours");
        }
        //go ahead and delete snippet
        db.delete(foundSnippet);
        log.info(String.format("snippet DELETED %d", id));
    }

}
