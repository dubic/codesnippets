/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.models;

import com.dubic.codesnippets.dto.SnipData;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dubem
 */
@Entity
@Table(name = "snippets")
public class Snippet implements Serializable {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Size(min = 1, max = 255, message = "title must be between 1 to 255 characters")
    @NotEmpty(message = "Empty value title not accepted")
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "dep")
    private String dependecies;
    @Column(name = "lang")
    private String lang;
    @Column(name = "code")
    @Lob
    private String code;
    @NotNull(message = "user must not be null")
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_dt")
    private Date created = new Date();
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "modified_dt")
    private Date modified;
    @Column(name = "views")
    private int views;
    @Column(name = "private")
    private boolean PRIVATE = false;

    public Snippet() {
    }

    public Snippet(SnipData sd) {
        this.title = sd.getTitle();
        this.code = sd.getCode();
        this.dependecies = sd.getDeps();
        this.description = sd.getDesc();
        this.lang = sd.getLang();
        this.id = sd.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDependecies() {
        return dependecies;
    }

    public void setDependecies(String dependecies) {
        this.dependecies = dependecies;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean isPRIVATE() {
        return PRIVATE;
    }

    public void setPRIVATE(boolean PRIVATE) {
        this.PRIVATE = PRIVATE;
    }
    

}
