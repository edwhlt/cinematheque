/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Find
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Find {

    @JsonProperty("movie_results")
    private List<DbMovie> dbMovies;
    @JsonProperty("person_results")
    private List<Person> people;
    @JsonProperty("tv_results")
    private List<DbSerie> series;

    public List<DbMovie> getDbMovies() {
        return dbMovies;
    }

    public List<Person> getPeople() {
        return people;
    }

    public List<DbSerie> getSeries() {
        return series;
    }

}
