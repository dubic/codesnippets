/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.spi;

/**
 *
 * @author dubem
 */
public class CodeSnippetsException extends RuntimeException{

    public CodeSnippetsException() {
    }

    public CodeSnippetsException(String message) {
        super(message);
    }

    public CodeSnippetsException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
