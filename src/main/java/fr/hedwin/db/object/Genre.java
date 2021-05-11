/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Genre
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.db.model.NamedIdElement;

import java.util.List;

public class Genre extends NamedIdElement {

    public static class GenreList {

        @JsonProperty("genres")
        private List<Genre> genres;

        public List<Genre> getGenres() {
            return genres;
        }

    }

}

