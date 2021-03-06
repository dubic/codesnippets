/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dubic.codesnippets.models;

import com.dubic.codesnippets.dto.UserData;
import com.dubic.codesnippets.util.IdmUtils;
import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * holds user details in database.uses email as username but user can create
 * screen name
 *
 * @author dubic
 * @since idm 1.0.0
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "user.find.mail.password", query = "SELECT u FROM User u WHERE u.password = :pwd and u.email = :email"),
    @NamedQuery(name = "user.findmail.email", query = "SELECT u.email FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "user.find.screenName", query = "SELECT u FROM User u WHERE u.screenName = :screenName"),
    @NamedQuery(name = "user.findscreename.scname", query = "SELECT u.screenName FROM User u WHERE u.screenName = :scname"),
    @NamedQuery(name = "user.find.id.mail", query = "SELECT u FROM User u WHERE u.email = :email and u.id = :id"),
    @NamedQuery(name = "user.find.mail", query = "SELECT u FROM User u WHERE u.email = :email")})
public class User implements UserDetails, Serializable {

    private Long id;
    private String screenName;
    private String email;
    private boolean showEmail = true;
    private String password;
    private String firstname;
    private String lastname;
    private String picture;
    private Date lastLoginDate;
    private boolean activated;
    private String phone;
    private Date createDate = new Date();
    private Date modifiedDate = new Date();
    private List<Role> roles = new ArrayList<Role>();

    public User() {
    }

    public User(UserData userData) {
        this.email = userData.getEmail();
        this.screenName = userData.getScreenName();
        this.password = userData.getPassword();
    }

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Size(min = 4, max = 20, message = "screen name must be between 4 and 20 chars")
    @NotEmpty(message = "Empty screen name")
    @Column(unique = true, length = 20, name = "screen_name")
    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @NotEmpty(message = "Email not set")
    @Email(message = "Email not valid")
    @Column(unique = true, name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "firstname", length = 50)
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Column(name = "lastname", length = 50)
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Column(name = "last_login_dt")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Column(name = "picture")
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Column(name = "active")
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Column(unique = true, name = "phone", length = 25)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @OneToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = {
                @JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "role_id", referencedColumnName = "id")})
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Column(name = "create_dt")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "modified_dt")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Size(min = 6, max = 255, message = "password must be between 6 - 255 chars")
    @NotEmpty(message = "User must have a password")
    @Column(name = "password")
    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "show_email")
    public boolean isShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static void main(String[] agggh) {
        User u = new User();
        u.setScreenName("dubic");
        u.setEmail("ddd@mmm.com");
        try {
            IdmUtils.validate(u);
        } catch (ConstraintViolationException ex) {
            for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
                System.out.println("msg - " + v.getMessage());
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return this.id == other.id || (this.id != null && this.id.equals(other.id));
    }

}
