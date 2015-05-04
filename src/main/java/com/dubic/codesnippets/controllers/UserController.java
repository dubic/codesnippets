/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.controllers;

import com.dubic.codesnippets.dto.UserData;
import com.dubic.codesnippets.models.NotificationSettings;
import com.dubic.codesnippets.models.User;
import com.dubic.codesnippets.spi.CodeSnippetsException;
import com.dubic.codesnippets.spi.Database;
import com.dubic.codesnippets.spi.IdentityService;
import com.dubic.codesnippets.spi.NotificationService;
import com.dubic.codesnippets.spi.SnippetService;
import com.dubic.codesnippets.util.IdmCrypt;
import com.dubic.codesnippets.util.IdmUtils;
import com.dubic.codesnippets.util.InvalidException;
import com.dubic.codesnippets.util.Validate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileInputStream;
import java.io.IOException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
    @Value("${picture.location}")
    private String picturePath;
    @Value("${default.profile.picture}")
    private String defaultAvatar;
//    @Inject
//    private MailServiceImpl mailService;
    

    @RequestMapping("/load")
    public String loadPage(@RequestParam("p") String page) {
        return "users/" + page;
    }

    @RequestMapping("/settings/load")
    public String loadSettignsPage(@RequestParam("p") String page) {
        return "settings/" + page;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)//headers="Accept=application/json"
    public UserData screenNameExists() {
        System.out.println("SCREEN NAME EXISTS");
        return new UserData("dubic", "udubic@gmail.com", "dcamic");
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
        resp.addProperty("firstname", user.getFirstname());
        resp.addProperty("lastname", user.getLastname());
        resp.addProperty("showEmail", user.isShowEmail());
        resp.addProperty("createDt", IdmUtils.formatDate(user.getCreateDate()));
        return resp;
    }

    @RequestMapping("/authenticated")
    public @ResponseBody
    JsonObject isAuthenticated() {
        JsonObject resp = new JsonObject();
        log.warn(SecurityContextHolder.getContext().getAuthentication());
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            resp.addProperty("authenticated", false);
        } else {
            resp.addProperty("authenticated", true);
        }

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
            user.addProperty("id", find.getId());
            user.addProperty("email", find.getEmail());
            user.addProperty("username", find.getScreenName());
            user.addProperty("picture", find.getPicture());
            user.addProperty("firstname", find.getFirstname());
            user.addProperty("lastname", find.getLastname());
        } catch (EntityNotFoundException enfe) {
            log.warn("NO ENTITY HIT. PLS CHECK ID : " + id);
        }
        return user;
    }

    @RequestMapping(value = "/profile/{name}")
    public @ResponseBody
    JsonObject getUserProfile(@PathVariable("name") String name) {
        JsonObject user = new JsonObject();
        try {
            User me = idmService.getUserLoggedIn();
            User find = idmService.findUserByScreenName(name);
            if (find == null) {
                user.addProperty("code", 404);
                return user;
            }
            if (me == null) {
                user.addProperty("me", false);
            } else {
                user.addProperty("me", me.getId().equals(find.getId()));
            }
            user.addProperty("code", 0);
            user.addProperty("id", find.getId());
            user.addProperty("email", find.getEmail());
            user.addProperty("username", find.getScreenName());
            user.addProperty("picture", find.getPicture());
            user.addProperty("firstname", find.getFirstname());
            user.addProperty("lastname", find.getLastname());
            user.addProperty("showEmail", find.isShowEmail());
            user.addProperty("createDt", IdmUtils.formatDate(find.getCreateDate()));
        } catch (EntityNotFoundException enfe) {
            log.warn("NO ENTITY HIT. PLS CHECK ID : " + name);
        }
        return user;
    }

    @RequestMapping(value = "/users-email")
    public @ResponseBody
    JsonArray getUserByNameEmail(@RequestParam("val") String search) {
        return idmService.getuserByEmailUsername(search);
    }

    @RequestMapping(value = "/img/{pic}")
    public void processImage(HttpServletRequest request, HttpServletResponse response, @PathVariable("pic") String picId) {
        ServletOutputStream responseStream = null;
        FileInputStream pictureStream = null;
        try {
            response.setContentType("image/jpeg");
            responseStream = response.getOutputStream();

            if (!picId.equalsIgnoreCase("male") || picId.equalsIgnoreCase("404")) {
                pictureStream = new FileInputStream(picturePath + picId);
                IOUtils.copy(pictureStream, responseStream);
            } else {
                //send avatar
                pictureStream = new FileInputStream(picturePath + "male.jpg");
                IOUtils.copy(pictureStream, responseStream);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        } finally {
            try {
                responseStream.close();
                pictureStream.close();
            } catch (IOException ex) {
                log.fatal(ex.getMessage());
            }
        }
    }

    @RequestMapping(value = {"/picture/upload"}, method = {RequestMethod.POST}, produces = "text/plain")
    @ResponseBody
    public String uploadPicture(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            System.out.println("name - " + file.getName());
            System.out.println("type - " + file.getContentType());
//            try {
//                System.out.println("size - "+file.getBytes().length);
//                file.transferTo(new File("C:\\Users\\Dubic\\Documents\\pictest."+file.getContentType().split("/")[1]));
            try {
                return this.idmService.changePicture(file.getInputStream());
            } catch (Exception e) {
                this.log.error(e.getMessage());
                return this.defaultAvatar;
            }
//            } catch (IOException ex) {
//                java.util.logging.LogManager.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
//            }

        }
        this.log.warn("No file was recieved");
        return "male.jpg";
    }

    
   
}
