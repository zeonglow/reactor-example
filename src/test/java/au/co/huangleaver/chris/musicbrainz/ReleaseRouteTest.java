package au.co.huangleaver.chris.musicbrainz;

import au.co.huangleaver.chris.musicbrainz.model.Release;
import au.co.huangleaver.chris.musicbrainz.repository.ReleaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReleaseRouteTest {

    @MockBean
    ReleaseRepository releaseRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    private void resetMocks() {
        Mockito.reset(releaseRepository);
    }

    @Test
    @DisplayName("WHEN no query parameter set THEN expect BadRequest")
    public void testRelease() {
        webTestClient
                .get().uri("/release")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("WHEN with valid query parameter set AND empty result THEN expect ok and valid JSON")
    public void testReleaseWithQueryParam() {
        Mockito.when(releaseRepository.getReleasesByArid(Mockito.any())).thenReturn(Mono.empty());
        webTestClient
                .get().uri(uriBuilder -> uriBuilder.path("/release").queryParam("arid", "hello").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk().expectBody().jsonPath("$", "");
    }

    @Test
    @DisplayName("WHEN with valid query parameter set AND two releases THEN expect ok and valid JSON")
    public void testReleaseWithQueryParam2() {
        List<Release> fooRelease = List.of(
                new Release().setArtistName("FooBar2").setTitle("Doof doof").setCountry("UK"),
                new Release().setArtistName("FooBar2").setTitle("Doof doof doof").setCountry("AU")
        );
        Mockito.when(releaseRepository.getReleasesByArid(Mockito.any())).thenReturn(Mono.just(fooRelease));
        webTestClient
                .get().uri(uriBuilder -> uriBuilder.path("/release").queryParam("arid", "hello").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.[*].country", "UK", "AU");
    }
}
