package org.koko.kokoprojectone.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class Comments {

    public int getId() {
        return id;
    }

    @Id
    @GeneratedValue
    private int id;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @NotNull
    private String comment;

    public Date getDate() {
        return date;
    }

    private Date date;

    public AnimePost getAnimePost() {
        return animePost;
    }

    public void setAnimePost(AnimePost animePost) {
        this.animePost = animePost;
    }

    @ManyToOne
    @JoinColumn(name="animePost_id", nullable=false)
    private AnimePost animePost;

    public Comments(String comment){
        this.comment = comment;
        this.date = new Date();
    }

    public Comments(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public String dateFormat(){
        String pattern = "dd MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern,new Locale("en", "US"));
        String dat = simpleDateFormat.format(date);
        return dat;
    }

}
