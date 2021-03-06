/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.controllers;

import com.dubic.codesnippets.dto.UniqueValidation;
import com.dubic.codesnippets.dto.UserData;
import com.dubic.codesnippets.email.MailServiceImpl;
import com.dubic.codesnippets.email.SimpleMailEvent;
import com.dubic.codesnippets.models.User;
import com.dubic.codesnippets.spi.IdentityService;
import com.dubic.codesnippets.util.IdmCrypt;
import com.dubic.codesnippets.util.IdmUtils;
import com.dubic.codesnippets.util.InvalidException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author dubem
 */
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final Logger log = Logger.getLogger(getClass());
    @Autowired
    private IdentityService idmService;
    @Autowired
    private MailServiceImpl mailService;

    @RequestMapping("/load")
    public String loadPage(@RequestParam("p") String page) {
        return "registration/" + page;
    }

    @RequestMapping("/signup")
    public @ResponseBody
    JsonObject signup(@RequestBody UserData userData) {
        log.debug("SIGNUP : " + new Gson().toJson(userData));
        JsonObject resp = new JsonObject();
        try {
            User user = idmService.userRegistration(userData);
            resp.addProperty("code", 0);
            resp.addProperty("id", user.getId());
            resp.addProperty("msg", "your account has been created. An email will be sent to you shortly to activate your account");
        } catch (ConstraintViolationException cve) {
            for (ConstraintViolation<?> constraintViolation : cve.getConstraintViolations()) {
                resp.addProperty("code", 500);
                resp.addProperty("msg", constraintViolation.getMessage());
                log.error(constraintViolation.getMessage());
                break;
            }
            log.error(cve.getMessage());

        } catch (EntityExistsException e) {
            log.warn(e.getMessage());
            resp.addProperty("code", 403);
            resp.addProperty("msg", e.getMessage());
        } catch (Exception e) {
            log.fatal(e.getMessage(), e);
            resp.addProperty("code", 500);
            resp.addProperty("msg", "An error occurred");
        }
        return resp;
    }

//    @RequestMapping(value = "/activate/{token}")
//    public String activate(Model m, @PathVariable String token) {
//        try {
//            idmService.activateUser(token);
//            //redirect to success activation
//            m.addAttribute("error", false);
//            m.addAttribute("linkExpired", Boolean.FALSE);
//            m.addAttribute("linkError", Boolean.FALSE);
//        } catch (InvalidTokenException ex) {
//            m.addAttribute("error", true);
//            m.addAttribute("linkExpired", Boolean.FALSE);
//            m.addAttribute("linkError", Boolean.TRUE);
//            log.error(ex.getMessage());
//        } catch (LinkExpiredException ex) {
//            m.addAttribute("error", true);
//            m.addAttribute("linkExpired", Boolean.TRUE);
//            m.addAttribute("linkError", Boolean.FALSE);
//            log.error(ex.getMessage());
//        } catch (Exception ex) {
//            m.addAttribute("error", true);
//            m.addAttribute("linkExpired", Boolean.FALSE);
//            m.addAttribute("linkError", Boolean.FALSE);
//            m.addAttribute("Service error, Try some time later", Boolean.TRUE);
//            log.error(ex.getMessage());
//        }
//        return "activation-outcome";
//    }
//
    @RequestMapping(value = "password/forgot")
    public @ResponseBody
    JsonObject forgotPassword(@RequestParam("email") String email, HttpSession session) {
        JsonObject resp = new JsonObject();
//find if user is in system
        User user = idmService.findUserByEmail(email);
        if (user == null) {
            resp.addProperty("code", 404);
            resp.addProperty("msg", String.format("%s does not exist", email));
            return resp;
        }
//generate pin and store user in session
        String pin = IdmCrypt.generateTimeToken();
        log.info(pin);
        session.setAttribute("password-reset-email", email);
        session.setAttribute("password-reset-pin", pin);

        SimpleMailEvent mail = new SimpleMailEvent(user.getEmail());
        Map model = new HashMap();
        model.put("token", pin);
        mailService.sendMail(mail, "password.vm", model);
        resp.addProperty("code", 0);
        resp.addProperty("msg", String.format("Enter passcode sent to your email [%s]", email));
        return resp;
    }

    @RequestMapping(value = "password/reset")
    public @ResponseBody
    JsonObject resetPassword(@RequestBody UserData userData, HttpSession session) {
        JsonObject resp = new JsonObject();
//find if pin is in session
        String email = (String) session.getAttribute("password-reset-email");
        String pin = (String) session.getAttribute("password-reset-pin");
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(pin)) {
            resp.addProperty("code", 404);
            resp.addProperty("msg", "the passcode you entered has expired");
            return resp;
        }
        if (!pin.equals(userData.getPasscode())) {
            resp.addProperty("code", 405);
            resp.addProperty("msg", "wrong passcode");
            return resp;
        }
        User user = idmService.findUserByEmail(email);
//generate pin and store user in session
        user.setPassword(userData.getPassword());
        idmService.resetPassword(user);

        resp.addProperty("code", 0);
        resp.addProperty("msg", "your password has been changed");
        return resp;
    }

    @RequestMapping("/resend/{email}/{id}")
    public @ResponseBody
    JsonObject resendActivation(@PathVariable("email") String email, @PathVariable("id") Long id) {
        JsonObject resp = new JsonObject();
        try {
            IdmUtils.validate(email).notEmpty("Invalid email");
            idmService.resendActivation(email.trim(), id);
            resp.addProperty("code", 0);
        } catch (InvalidException ex) {
            resp.addProperty("code", 400);
            resp.addProperty("msg", ex.getMessage());
        } catch (EntityExistsException eee) {
            resp.addProperty("code", 401);
            resp.addProperty("msg", eee.getMessage());
        } catch (EntityNotFoundException enfe) {
            log.warn("ENITIY LOSS HIT ID : " + id);
            resp.addProperty("code", 500);
            resp.addProperty("msg", "Server error occured");
        }
        return resp;
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
}
