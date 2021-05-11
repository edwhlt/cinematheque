/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Movie
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.*;

public class Movie {

    public enum Format {
        @JsonProperty("DVD") DVD("DVD"), @JsonProperty("BLURAY") BLURAY("Bluray"), @JsonProperty("NUMERIQUE") NUMERIQUE("Numérique");
        private final String indice;
        Format(String indice){
            this.indice = indice;
        }
        public static Format getIndice(String indice){
            return Arrays.stream(Format.values()).filter(t -> t.indice.equals(indice)).findFirst().orElse(null);
        }
        @Override
        public String toString() {
            return indice;
        }
    }

    @JsonProperty("id_tmdb_link")
    private int idTmdbLink;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("format")
    private Format format;

    @JsonProperty("comments")
    private Map<UUID, Comment> comments = new HashMap<>();

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date date;

    private Movie(){}

    public Movie(int idTmdbLink, String nom, Format numerique, Date date) {
        this.idTmdbLink = idTmdbLink;
        this.nom = nom;
        this.format = numerique;
        this.date = date;
    }

    public Movie(String nom, Format numerique, Date date) {
        this(-1, nom, numerique, date);
    }

    public int getIdTmdbLink() {
        return idTmdbLink;
    }

    @JsonSetter("id_tmdb_link")
    public void setIdTmdbLink(int idTmdbLink) {
        if(idTmdbLink == 0) this.idTmdbLink = -1;
        else this.idTmdbLink = idTmdbLink;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Format getFormat() {
        return format;
    }

    public Map<UUID, Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment){
        comments.put(comment.getUuid(), comment);
    }

    public void removeComment(UUID commentUuid) {
        comments.remove(commentUuid);
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    @JsonIgnore
    public int getNote() {
        return comments == null || comments.isEmpty() ? 0 : comments.values().stream().mapToInt(Comment::getNote).sum()/comments.size();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
