/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Cast
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.db.model.NamedIdElement;

public class Cast extends NamedIdElement {

    @JsonProperty("adult")
    private boolean adult;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("known_for_department")
    private String knownForDepartment;
    @JsonProperty("original_name")
    private String originalName;
    @JsonProperty("popularity")
    private int popularity;
    @JsonProperty("profile_path")
    private String profilePath;
    @JsonProperty("cast_id")
    private int castId;
    @JsonProperty("character")
    private String character;
    @JsonProperty("credit_id")
    private String creditId;
    @JsonProperty("order")
    private int order;

    public boolean isAdult() {
        return adult;
    }

    public int getGender() {
        return gender;
    }

    public String getKnownForDepartment() {
        return knownForDepartment;
    }

    public String getOriginalName() {
        return originalName;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public int getCastId() {
        return castId;
    }

    public String getCharacter() {
        return character;
    }

    public String getCreditId() {
        return creditId;
    }

    public int getOrder() {
        return order;
    }
}
