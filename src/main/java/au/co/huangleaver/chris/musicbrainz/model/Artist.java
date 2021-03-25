package au.co.huangleaver.chris.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {

    private String alias;
    private String arid;
    private String artist;
    private String artistaccent;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Artist{");
        sb.append("alias='").append(alias).append('\'');
        sb.append(", arid='").append(arid).append('\'');
        sb.append(", artist='").append(artist).append('\'');
        sb.append(", artistaccent='").append(artistaccent).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", comment='").append(comment).append('\'');
        sb.append('}');
        return sb.toString();
    }

    private String country;
    private String comment;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getArid() {
        return arid;
    }

    public void setArid(String arid) {
        this.arid = arid;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistaccent() {
        return artistaccent;
    }

    public void setArtistaccent(String artistaccent) {
        this.artistaccent = artistaccent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
