/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dubic.codesnippets.spi;

/**
 *
 * @author dubem
 */
public class NotMeException extends CodeSnippetsException {

    /**
     * Creates a new instance of <code>NotMeException</code> without detail
     * message.
     */
    public NotMeException() {
    }

    /**
     * Constructs an instance of <code>NotMeException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public NotMeException(String msg) {
        super(msg);
    }

    public NotMeException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
