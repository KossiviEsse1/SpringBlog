package org.koko.kokoprojectone.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3)
    private String username;

    @NotNull
    private String email;

    @NotNull
    @Size(min=9)
    private String password;


    private boolean active;


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String role;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


//    public User(String username, String password, String email, boolean active, String role){
//        this.username=username;
//        this.password=password;
//        this.email=email;
//        this.active=active;
//        this.role = "ROLE_USER";
//    }
//
//    public User(){}

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy="user")
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy="user")
    private List<AnimePost> animePosts = new ArrayList<>();

    @OneToMany(mappedBy="user")
    private List<Likes> likes = new ArrayList<>();
}
