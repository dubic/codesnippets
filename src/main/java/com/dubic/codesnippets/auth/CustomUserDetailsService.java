/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.auth;

import com.dubic.codesnippets.spi.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author DUBIC
 */

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IdentityService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userService.findUserByEmail(username);
    }

}
