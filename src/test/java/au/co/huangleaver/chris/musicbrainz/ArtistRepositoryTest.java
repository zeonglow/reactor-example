package au.co.huangleaver.chris.musicbrainz;

import au.co.huangleaver.chris.musicbrainz.model.Artist;
import au.co.huangleaver.chris.musicbrainz.repository.ArtistRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Configurable
@Disabled("This call the real MusicBrainz API which fails on occasion")
public class ArtistRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(ArtistRepositoryTest.class);

    @Value("${webclient.timeout:3}")
    private long webClientTimeoutSec;

    @Test
    public void getBonobo(@Autowired ArtistRepository ar) {
        var mono = ar.getArtistByName("bonobo");

        List<Artist> artists = mono.block(Duration.ofSeconds(webClientTimeoutSec));

        assertThat(artists).isNotNull();
        log.info("artists size: {}", artists.size());
        var artist = artists.get(0);
        assertThat(artist).isNotNull();
        assertThat(artist.getArid()).isNotEmpty();
        assertThat(artist.getCountry()).isNotEmpty();
        assertThat(artist.getDisambiguation()).isNotEmpty();
        assertThat(artist.getName()).isNotEmpty();
    }
}
