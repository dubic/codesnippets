/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.spi;

import com.dubic.codesnippets.models.Notification;
import com.dubic.codesnippets.models.NotificationSettings;
import com.dubic.codesnippets.models.User;
import com.dubic.codesnippets.util.IdmUtils;
import com.google.gson.Gson;
import java.util.Date;
import java.util.List;
import javax.inject.Named;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

/**
 * LogManager.getLogger
 *
 * @author Dubic
 */
@Named("notificationService")
public class NotificationService {

    private final Logger log = Logger.getLogger(NotificationService.class);

    @Autowired
    private Database db;
    @Autowired
    private IdentityService idmService;
//    @Autowired
//    private MailServiceImpl mailService;

    public NotificationSettings getSettings() {
        User user = idmService.getUserLoggedIn();
        NotificationSettings notif = IdmUtils.getFirstOrNull(db.createQuery("SELECT n FROM NotificationSettings n WHERE n.user.id = :id", NotificationSettings.class)
                .setParameter("id", user.getId()).getResultList());
        if (notif == null) {
            //no notif found create one. user must have a row
            NotificationSettings n = new NotificationSettings(user);
            db.persist(n);
            log.info("Notification created because none was found for the user : " + user.getId());
            return n;
        }
        return notif;
    }

    public NotificationSettings getSettings(User user) {
        NotificationSettings notif = IdmUtils.getFirstOrNull(db.createQuery("SELECT n FROM NotificationSettings n WHERE n.user.id = :id", NotificationSettings.class)
                .setParameter("id", user.getId()).getResultList());
        if (notif == null) {
            //no notif found create one. user must have a row
            NotificationSettings n = new NotificationSettings(user);
            db.persist(n);
            log.info("Notification created because none was found for the user : " + user.getId());
            return n;
        }
        return notif;
    }

    public NotificationSettings saveSettings(NotificationSettings n) {
        log.debug(new Gson().toJson(n));
        NotificationSettings notif = getSettings();
        notif.setEmail(n.isEmail());
        notif.setWeb(n.isWeb());
        notif.setModified(new Date());
        db.merge(notif);
        log.info("Notification saved : " + notif.getId());
        return notif;
    }

    @Async
    public void notify(NotificationSettings setting, String msg, Notification.Type type, String param) {
        if (setting.isWeb()) {
            Notification n = new Notification();
            n.setMsg(msg);
            n.setOwner(setting.getUser());
            n.setParam(param);
            n.setType(type);

            db.persist(n);
            log.info("Notification for web");
        }
        if (setting.isEmail()) {
            //KINDLY SEND MAIL
        }
    }

    public Long getUnreadCount(Long userid) {
        return db.createQuery("SELECT COUNT(n.id) FROM Notification n WHERE n.owner.id = :uid  AND n.seen = FALSE", Long.class)
                .setParameter("uid", userid).getSingleResult();
    }

    public List<Notification> getUnreadNotifications(Long userid, int start, int size) {
        List<Notification> notifications = db.createQuery("SELECT n FROM Notification n WHERE n.owner.id = :uid AND n.seen = FALSE", Notification.class)
                .setParameter("uid", userid).setFirstResult(start).setMaxResults(size).getResultList();
        return notifications;
    }

    @Async
    @Transactional
    public void readNotifications(List<Notification> notifications) {
        for (Notification n : notifications) {
            n.setSeen(true);
            db.merge(n);
        }
    }
}
