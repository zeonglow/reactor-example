package au.co.huangleaver.chris.musicbrainz.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseQueryResponse {
    private Date created;
    private Integer count;
    private List<Release> releases;

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

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

    @Override
    public String toString() {
        return "ReleaseQueryResponse{" +
                "created=" + created +
                ", count=" + count +
                ", releases=" + releases +
                '}';
    }
}
