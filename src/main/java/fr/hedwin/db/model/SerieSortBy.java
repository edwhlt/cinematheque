/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: SerieSortBy
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.model;

import java.util.Arrays;

public enum SerieSortBy {

    popularity_asc("popularity.asc", "Popularité ↗"),
    popularity_desc("popularity.desc", "Popularité ↘"),
    first_air_date_asc("first_air_date.asc", "Date ↗"),
    first_air_date_desc("first_air_date.desc", "Date ↘"),
    vote_average_asc("vote_average.asc", "Note moyenne ↗"),
    vote_average_desc("vote_average.desc", "Note moyenne ↘");

    private String key;
    private String name;

    SerieSortBy(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getKey();
    }

    public static SerieSortBy getSort(String key){
        return Arrays.stream(SerieSortBy.values()).filter(sortBy -> sortBy.key.equals(key)).findFirst().orElse(null);
    }
    public static SerieSortBy getSortByName(String key){
        return Arrays.stream(SerieSortBy.values()).filter(sortBy -> sortBy.name.equals(key)).findFirst().orElse(null);
    }

    public static SerieSortBy getDefault(){
        return SerieSortBy.popularity_desc;
    }
}
