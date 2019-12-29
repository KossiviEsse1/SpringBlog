package org.koko.kokoprojectone.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Likes {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private AnimePost animePost;

    public Likes(User user, AnimePost animePost) {
        this.user = user;
        this.animePost = animePost;
    }

    public Likes() {
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AnimePost getAnimePost() {
        return animePost;
    }

    public void setAnimePost(AnimePost animePost) {
        this.animePost = animePost;
    }

    public boolean equals(Likes o) {

        if (o == this) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (o.getClass() != getClass()) {
            return false;
        }

        Likes like = (Likes) o;
        return (like.getUser() == getUser() && like.getAnimePost() == getAnimePost());
    }
}
