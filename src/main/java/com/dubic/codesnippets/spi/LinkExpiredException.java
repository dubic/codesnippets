/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.spi;

/**
 *
 * @author dubem
 */
public class LinkExpiredException extends CodeSnippetsException{

    public LinkExpiredException() {
    }

    public LinkExpiredException(String message) {
        super(message);
    }

    public LinkExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
