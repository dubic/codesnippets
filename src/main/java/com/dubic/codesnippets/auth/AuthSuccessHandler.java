/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.auth;

import com.dubic.codesnippets.models.User;
import com.dubic.codesnippets.spi.IdentityService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 *
 * @author dubem
 */
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger log = Logger.getLogger(getClass());
    @Autowired private IdentityService idmService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest hsr, HttpServletResponse hsr1, Authentication a) throws IOException, ServletException {
        log.info(String.format("[%s] successfully authenticated", (String) a.getPrincipal()));
        hsr1.setContentType("application/json");
        hsr1.setStatus(HttpServletResponse.SC_OK);
        User user = idmService.findUserByEmail((String) a.getPrincipal());
        JsonObject resp = new JsonObject();
        
        resp.addProperty("code", 0);
        resp.addProperty("id", user.getId());
        resp.addProperty("email", user.getEmail());
        resp.addProperty("picture", user.getPicture());
        resp.addProperty("screenName", user.getScreenName());
        hsr1.getWriter().write(new Gson().toJson(resp));
        hsr1.getWriter().flush();
    }

}
