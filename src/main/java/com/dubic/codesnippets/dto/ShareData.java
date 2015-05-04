/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dubic
 */
public class ShareData {
    private List<Share> sharelist = new ArrayList<Share>();
    private String msg;

    public List<Share> getSharelist() {
        return sharelist;
    }

    public void setSharelist(List<Share> sharelist) {
        this.sharelist = sharelist;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
}
