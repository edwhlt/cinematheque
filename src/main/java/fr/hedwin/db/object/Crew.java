/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Crew
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.db.model.NamedIdElement;

public class Crew extends NamedIdElement {

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
    @JsonProperty("credit_id")
    private String creditId;
    @JsonProperty("department")
    private String department;
    @JsonProperty("job")
    private String job;

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

    public String getCreditId() {
        return creditId;
    }

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
    }

}
