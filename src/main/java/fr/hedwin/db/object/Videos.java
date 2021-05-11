/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Videos
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.db.model.IdElement;

import java.util.List;
import java.util.stream.Collectors;

public class Videos extends IdElement {

    @JsonProperty("results")
    private List<Video> results;

    public List<Video> getVideoList() {
        return results;
    }

    public List<Video> getTrailers(){
        return results.stream().filter(v -> v.type.equals(VideoType.Trailer)).collect(Collectors.toList());
    }

    public Video getVideo(VideoType videoType, int index){
        return results.stream().filter(v -> v.getType().equals(videoType)).collect(Collectors.toList()).get(index);
    }

    public static enum VideoType{
        @JsonProperty("Trailer") Trailer, @JsonProperty("Teaser") Teaser, @JsonProperty("Clip") Clip,
        @JsonProperty("Featurette") Featurette, @JsonProperty("Behind the Scenes") BehindTheScenes, @JsonProperty("Bloopers") Bloopers
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Video {
        @JsonProperty("id")
        private String id;

        @JsonProperty("iso_639_1")
        private String iso_639_1;

        @JsonProperty("iso_3166_1")
        private String iso_3166_1;

        @JsonProperty("key")
        private String key;

        @JsonProperty("name")
        private String name;

        @JsonProperty("site")
        private String site;

        @JsonProperty("size")
        private int size;

        @JsonProperty("type")
        private VideoType type;

        public String getId() {
            return id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public int getSize() {
            return size;
        }

        public VideoType getType() {
            return type;
        }
    }


}
