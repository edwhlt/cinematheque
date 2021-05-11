/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: MovieSortBy
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.model;

import java.util.Arrays;

public enum MovieSortBy {

    popularity_asc("popularity.asc", "Popularité ↗"),
    popularity_desc("popularity.desc", "Popularité ↘"),
    release_date_asc("release_date.asc", "Date ↗"),
    release_date_desc("release_date.desc", "Date ↘"),
    revenue_asc("revenue.asc", "Revenue ↗"),
    revenue_desc("revenue.desc", "Revenue ↘"),
    primary_release_date_asc("primary_release_date.asc", "Première Date ↗"),
    primary_release_date_desc("primary_release_date.desc", "Date ↘"),
    original_title_asc("original_title.asc", "Titre ↗"),
    original_title_desc("original_title.desc", "Titre ↘"),
    vote_average_asc("vote_average.asc", "Note moyenne ↗"),
    vote_average_desc("vote_average.desc", "Note moyenne ↘"),
    vote_count_asc("vote_count.asc", "Nombre de vote ↗"),
    vote_count_desc("vote_count.desc", "Nombre de Vote ↘");

    private String key;
    private String name;

    MovieSortBy(String key, String name) {
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

    public static MovieSortBy getSort(String key){
        return Arrays.stream(MovieSortBy.values()).filter(sortBy -> sortBy.key.equals(key)).findFirst().orElse(null);
    }
    public static MovieSortBy getSortByName(String key){
        return Arrays.stream(MovieSortBy.values()).filter(sortBy -> sortBy.name.equals(key)).findFirst().orElse(null);
    }

    public static MovieSortBy getDefault(){
        return MovieSortBy.popularity_desc;
    }

}
