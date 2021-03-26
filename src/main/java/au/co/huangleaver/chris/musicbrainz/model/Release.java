package au.co.huangleaver.chris.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Release {

    @JsonProperty("id")
    private String mbid;
    private String country;
    private String date;
    private String status;
    private String title;

    @JsonProperty("artist-name")
    private String artistName;

    @JsonProperty("artist-credit")
    @SuppressWarnings("unchecked")
    private void unpackArtistCredit(List<Object> artistCredit) {
        if (!artistCredit.isEmpty()) {
            Map<String, String> artist = (Map<String, String>) artistCredit.get(0);
            artistName = artist.get("name");
        }
    }

    public String getMbid() {
        return mbid;
    }

    public Release setMbid(String mbid) {
        this.mbid = mbid;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Release setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Release setDate(String date) {
        this.date = date;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Release setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Release setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getArtistName() {
        return artistName;
    }

    public Release setArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    @Override
    public String toString() {
        return "Release{" +
                "mbid='" + mbid + '\'' +
                ", country='" + country + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}
