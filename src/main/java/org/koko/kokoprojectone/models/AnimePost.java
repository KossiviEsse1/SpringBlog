package org.koko.kokoprojectone.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
public class AnimePost {

    public AnimePost(String title, String description, byte[] postImage, String snippet) {
        this.title = title;
        this.description = description;
        this.postImage = postImage;
        this.snippet = snippet;
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public String dateFormat(){
        String pattern = "MMMMM dd yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern,new Locale("en", "US"));
        String dat = simpleDateFormat.format(date);
        return dat;
    }

    public AnimePost(){}

    public int getId() {
        return id;
    }

    @Id
    @GeneratedValue
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    @Size(min=3, max=25)
    private String title;

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @Size(max=50)
    private String snippet;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @Size(min=1)
    @Lob
    @Column(length = 100000)
    private String description;

    public byte[] getPostImage() {
        return postImage;
    }

    public void setPostImage(byte[] postImage) {
        this.postImage = postImage;
    }

    @Lob
    private byte[] postImage;

    public String getImageString(){
        String s = Base64.getEncoder().encodeToString(postImage);
        return s;
    }

    @OneToMany(mappedBy="animePost", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Comments> comments = new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToMany(mappedBy="animePost", orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    public List<Likes> getLikes() {
        return likes;
    }
}
