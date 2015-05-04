/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.controllers;

import com.dubic.codesnippets.models.Notification;
import com.dubic.codesnippets.spi.IdentityService;
import com.dubic.codesnippets.spi.NotificationService;
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
@RequestMapping("/notifications")
public class NotificationController {
    private final Logger log = Logger.getLogger(getClass());
    @Autowired
    private IdentityService idmService;
    @Autowired
    private NotificationService notifService;
    
    @RequestMapping(value = "/unread/count")
    public @ResponseBody
    JsonObject getUnreadCount() {
        JsonObject resp = new JsonObject();
        resp.addProperty("count",notifService.getUnreadCount(idmService.getUserLoggedIn().getId()));
        return resp;
    }
    
    @RequestMapping(value = "/unread/list")
    public @ResponseBody
    JsonObject getUnread(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) {
        JsonObject resp = new JsonObject();
        List<Notification> unreadNotifications = notifService.getUnreadNotifications(idmService.getUserLoggedIn().getId(),start,size);
        JsonArray nArray = new JsonArray();
        for (Notification n : unreadNotifications) {
            JsonObject no = new JsonObject();
            no.addProperty("id", n.getId());
            no.addProperty("msg", n.getMsg());
            no.addProperty("read", n.isSeen());
            no.addProperty("time", n.getCreated().getTime());
            no.addProperty("type", n.getType().toString());
            nArray.add(no);
        }
        resp.add("notifs",nArray);
        notifService.readNotifications(unreadNotifications);
        return resp;
    }
}
