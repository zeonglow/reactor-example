package au.co.huangleaver.chris.musicbrainz;

import au.co.huangleaver.chris.musicbrainz.model.Release;
import au.co.huangleaver.chris.musicbrainz.repository.ReleaseRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Disabled("This call the real MusicBrainz API which fails on occasion")
public class ReleaseRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(ReleaseRepositoryTest.class);

    @Value("${webclient.timeout:3}")
    private long webClientTimeoutSec;


    @Test
    public void getBonobo(@Autowired ReleaseRepository rr) {
        final String bonoboArid = "9a709693-b4f8-4da9-8cc1-038c911a61be";
        var mono = rr.getReleasesByArid(bonoboArid);
        List<Release> releaseList = mono.block(Duration.ofSeconds(webClientTimeoutSec));

        assertThat(releaseList).isNotNull();
        assertThat(releaseList).isNotEmpty();

        Release release = releaseList.get(0);
        assertThat(release.getDate()).isNotEmpty();
        assertThat(release.getCountry()).isNotEmpty();
        assertThat(release.getStatus()).isNotEmpty();
        assertThat(release.getTitle()).isNotEmpty();
        assertThat(release.getMbid()).isNotEmpty();
        assertThat(release.getCountry()).isNotEmpty();

    }
}
