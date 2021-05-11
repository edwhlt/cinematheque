/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: DbMovie
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.db.Results;
import fr.hedwin.db.TMDB;
import fr.hedwin.db.model.IdElement;
import fr.hedwin.db.utils.Future;

import java.util.List;
import java.util.function.Function;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DbMovie extends IdElement {


    public static class ResultsMovie extends Results<DbMovie> {
        public ResultsMovie(Function<Integer, Future<ResultsPage<DbMovie>>> pageFunction) throws Exception {
            super(pageFunction);
        }
    }


    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("adult")
    private boolean adult;

    @JsonProperty("budget")
    private long budget;

    @JsonProperty("genres")
    private List<Genre> genres;

    @JsonProperty("genre_ids")
    private List<Integer> genresIds;

    @JsonProperty("homepage")
    private String homepage;

    @JsonProperty("imdb_id")
    private String imdbID;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("title")
    private String title;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("vote_average")
    private String voteAverage;

    public String getPosterPath() {
        return posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public long getBudget() {
        return budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    @JsonIgnore
    public Future<Videos> getVideos(){
        return TMDB.getMovieVideos(getId());
    }

    @JsonIgnore
    public Future<Credits> getCredits(){
        return TMDB.getMovieCredits(getId());
    }

    @Override
    public String toString() {
        return "DbMovie{" +
                "posterPath='" + posterPath + '\'' +
                ", adult=" + adult +
                ", budget=" + budget +
                ", genres=" + genres +
                ", genresIds=" + genresIds +
                ", homepage='" + homepage + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", title='" + title + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", voteAverage='" + voteAverage + '\'' +
                "} " + super.toString();
    }
}
