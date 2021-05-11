/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: DbSerie
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.db.Results;
import fr.hedwin.db.model.NamedIdElement;
import fr.hedwin.db.utils.Future;

import java.util.List;
import java.util.function.Function;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DbSerie extends NamedIdElement {

    public static class ResultsTVSerie extends Results<DbSerie> {
        public ResultsTVSerie(Function<Integer, Future<ResultsPage<DbSerie>>> pageFunction) throws Exception {
            super(pageFunction);
        }
    }

    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("popularity")
    private int popularity;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("vote_average")
    private int voteAverage;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("first_air_date")
    private String firstAirDate;
    @JsonProperty("origin_country")
    private List<String> originCountry;
    @JsonProperty("genre_ids")
    private List<String> genreIds;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("vote_count")
    private int voteCount;
    @JsonProperty("original_name")
    private String originalName;

    public String getPosterPath() {
        return posterPath;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getOriginalName() {
        return originalName;
    }

    @Override
    public String toString() {
        return "DbSerie{" +
                "posterPath='" + posterPath + '\'' +
                ", popularity=" + popularity +
                ", backdropPath='" + backdropPath + '\'' +
                ", voteAverage=" + voteAverage +
                ", overview='" + overview + '\'' +
                ", firstAirDate='" + firstAirDate + '\'' +
                ", originCountry=" + originCountry +
                ", genreIds=" + genreIds +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", voteCount=" + voteCount +
                ", originalName='" + originalName + '\'' +
                "} " + super.toString();
    }
}
