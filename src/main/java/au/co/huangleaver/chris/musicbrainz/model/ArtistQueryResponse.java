package au.co.huangleaver.chris.musicbrainz.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistQueryResponse {
    private Date created;
    private Integer count;
    private Integer offset;
    private List<Artist> artists;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    @Override
    public String toString() {
        return "ArtistQueryResponse{" + "created='" + created + '\'' +
                ", count=" + count +
                ", offset=" + offset +
                ", artists=" + artists +
                '}';
    }

}
