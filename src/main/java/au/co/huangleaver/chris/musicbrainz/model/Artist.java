package au.co.huangleaver.chris.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {

    @JsonProperty("id")
    private String arid;
    private String name;
    private String country;
    private String disambiguation;

    public String getArid() {
        return arid;
    }

    public Artist setArid(String arid) {
        this.arid = arid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Artist setName(String name) {
        this.name = name;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Artist setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public Artist setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
        return this;
    }

    @Override
    public String toString() {
        return "Artist{" + "arid='" + arid + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                '}';
    }
}
