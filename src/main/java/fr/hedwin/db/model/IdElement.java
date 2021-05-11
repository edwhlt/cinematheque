/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: IdElement
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class IdElement implements TmdbElement {

    @JsonProperty("id")
    private int id;

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdElement idElement = (IdElement) o;
        return id == idElement.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
