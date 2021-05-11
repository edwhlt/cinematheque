/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Comment
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.Main;

import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

    @JsonProperty("uuid")
    private UUID uuid;
    @JsonProperty("movie_id")
    private UUID movieUuid;
    @JsonProperty("user_id")
    private UUID userUuid;
    @JsonProperty("title")
    private String title;
    @JsonProperty("note")
    private int note;
    @JsonProperty("content")
    private String content;
    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date date;

    public Comment(){}

    public Comment(UUID uuid, UUID movie, User user, String title, int note, String content, Date date) {
        this(uuid, movie, user.getUuid(), title, note, content, date);
    }

    public Comment(UUID uuid, UUID movieUuid, UUID userUuid, String title, int note, String content, Date date) {
        this.uuid = uuid;
        this.movieUuid = movieUuid;
        this.userUuid = userUuid;
        this.title = title;
        this.note = note;
        this.content = content;
        this.date = date;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getMovieUuid() {
        return movieUuid;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    @JsonIgnore
    public User getUser() {
        return Main.users.get(userUuid);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }
}
