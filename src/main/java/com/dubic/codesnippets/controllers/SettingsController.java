/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.controllers;

import com.dubic.codesnippets.models.NotificationSettings;
import com.dubic.codesnippets.models.User;
import com.dubic.codesnippets.spi.IdentityService;
import com.dubic.codesnippets.spi.NotificationService;
import com.dubic.codesnippets.util.IdmCrypt;
import com.dubic.codesnippets.util.IdmUtils;
import com.dubic.codesnippets.util.InvalidException;
import com.dubic.codesnippets.util.Validate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Dubic
 */
@Controller
@RequestMapping("/settings")
public class SettingsController {
    private final Logger log = Logger.getLogger(getClass());
    @Autowired
    private IdentityService idmService;
    @Autowired
    private NotificationService notifService;
    
     @RequestMapping(value = "/update")
    public @ResponseBody
    JsonObject update(@RequestBody JsonObject prms) {
        JsonObject resp = new JsonObject();
        System.out.println(new Gson().toJson(prms));
        try {
            String fname = prms.get("firstname") == null ? null : prms.get("firstname").getAsString();
            String lname = prms.get("lastname") == null ? null : prms.get("lastname").getAsString();
            String username = prms.get("screenName") == null ? null : prms.get("screenName").getAsString();

            User user = idmService.getUserLoggedIn();
            user.setFirstname(fname);
            user.setLastname(lname);
            user.setScreenName(username);
            idmService.updateUser(user);
            resp.addProperty("code", 0);
            resp.addProperty("id", user.getId());
            resp.addProperty("email", user.getEmail());
            resp.addProperty("picture", user.getPicture());
            resp.addProperty("screenName", user.getScreenName());
            resp.addProperty("firstname", user.getFirstname());
            resp.addProperty("lastname", user.getLastname());
            resp.addProperty("showEmail", user.isShowEmail());
            resp.addProperty("createDt", IdmUtils.formatDate(user.getCreateDate()));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            resp.addProperty("code", 500);
        }
        return resp;
    }

    @RequestMapping(value = "/validate-email")
    public @ResponseBody
    JsonObject validateEmail(@RequestParam("p") String email, HttpSession session) {
        JsonObject resp = new JsonObject();

        try {
            new Validate(email).notEmpty("email is required");
            String passcode = IdmCrypt.generateTimeToken();
            session.setAttribute(passcode, email.trim());
            //send email
            resp.addProperty("code", 0);
            log.debug("TOKEN : " + passcode);
        } catch (InvalidException ex) {
            log.warn(ex.getMessage(), ex);
            resp.addProperty("code", 302);
            resp.addProperty("msg", ex.getMessage());
        } catch (Exception ex) {
            log.fatal(ex.getMessage(), ex);
            resp.addProperty("code", 500);
            resp.addProperty("msg", "unexpected server error");
        }
        return resp;
    }

    @RequestMapping(value = "/change-email")
    public @ResponseBody
    JsonObject updateEmail(@RequestBody JsonObject prms, HttpSession session) {
        JsonObject resp = new JsonObject();
        System.out.println(new Gson().toJson(prms));
        try {
            String password = prms.get("pword") == null ? null : prms.get("pword").getAsString();
            String passcode = prms.get("pcode") == null ? null : prms.get("pcode").getAsString();
            //VALIDATE PASSCODE
            Object newEmail = session.getAttribute(passcode);
            if (newEmail == null) {
                throw new InvalidException("your passcode has expired");
            }

            idmService.changeEmail((String) newEmail, password);
            resp.addProperty("code", 0);
        } catch (InvalidException ex) {
            log.warn(ex.getMessage());
            resp.addProperty("code", 500);
            resp.addProperty("msg", ex.getMessage());

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            resp.addProperty("code", 500);
            resp.addProperty("msg", "unxepected server error occurred");
        }
        return resp;
    }

    @RequestMapping({"/change-password"})
    @ResponseBody
    public JsonObject changePassword(@RequestBody JsonObject params) {
        JsonObject resp = new JsonObject();
        try {
            String current = params.get("current") == null ? null : params.get("current").getAsString();
            String newpword = params.get("newpword") == null ? null : params.get("newpword").getAsString();

            new Validate(current).notEmpty("current passsword not supplied");
            new Validate(newpword).notEmpty("new passsword not supplied");
            this.idmService.changePassword(current, newpword);

            resp.addProperty("code", 0);
        } catch (InvalidException e) {
            log.warn(e.getMessage());
            resp.addProperty("code", 500);
            resp.addProperty("msg", e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resp.addProperty("code", 500);
            resp.addProperty("msg", "unxepected server error occurred");
        }
        return resp;
    }

    @RequestMapping({"/email-settings"})
    @ResponseBody
    public JsonObject emailSettings(@RequestBody JsonObject params) {
        JsonObject resp = new JsonObject();
        try {
            boolean showEmail = params.get("show") == null ? null : params.get("show").getAsBoolean();

            User user = this.idmService.getUserLoggedIn();
            user.setShowEmail(showEmail);
            idmService.updateUser(user);

            resp.addProperty("code", 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resp.addProperty("code", 500);
            resp.addProperty("msg", "unxepected server error occurred");
        }
        return resp;
    }

    @RequestMapping({"/notifcations/load"})
    @ResponseBody
    public JsonObject loadNotificationSettings() {
        JsonObject resp = new JsonObject();
        try {
//            User user =
            NotificationSettings notification = this.notifService.getSettings();

            resp.addProperty("code", 0);
            resp.addProperty("web", notification.isWeb());
            resp.addProperty("email", notification.isEmail());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resp.addProperty("code", 500);
            resp.addProperty("msg", "unxepected server error occurred");
        }
        return resp;
    }

    @RequestMapping({"/notifcations/update"})
    @ResponseBody
    public JsonObject updateNotificationSettings(@RequestBody NotificationSettings n) {
        JsonObject resp = new JsonObject();
        try {
            NotificationSettings notification = this.notifService.saveSettings(n);

            resp.addProperty("code", 0);
            resp.addProperty("web", notification.isWeb());
            resp.addProperty("email", notification.isEmail());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resp.addProperty("code", 500);
            resp.addProperty("msg", "unxepected server error occurred");
        }
        return resp;
    }
}
