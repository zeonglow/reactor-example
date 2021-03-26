package au.co.huangleaver.chris.musicbrainz;

import au.co.huangleaver.chris.musicbrainz.model.Artist;
import au.co.huangleaver.chris.musicbrainz.model.Release;
import au.co.huangleaver.chris.musicbrainz.repository.ArtistRepository;
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
public class ArtistReleaseRouteTest {

    @MockBean
    ArtistRepository artistRepositoryMock;
    @MockBean
    ReleaseRepository releaseRepositoryMock;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    private void resetMocks() {
        Mockito.reset(artistRepositoryMock);
        Mockito.reset(releaseRepositoryMock);
    }

    @Test
    @DisplayName("WHEN no query parameter set THEN expect BadRequest")
    public void testRelease() {
        webTestClient
                .get().uri("/artist-release")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("WHEN with valid query parameter set AND empty artist result THEN expect ok and valid JSON")
    public void testReleaseWithQueryParam() {
        Mockito.when(artistRepositoryMock.getArtistByName(
                Mockito.any())).thenReturn(Mono.just(List.of()));

        webTestClient
                .get().uri(uriBuilder -> uriBuilder.path("/artist-release").queryParam("name", "hello").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk().expectBody().jsonPath("$", "");
    }

    @Test
    @DisplayName("WHEN artistRepository fails THEN expect failure")
    public void testReleaseWithDownStreamError() {
        Mockito.when(artistRepositoryMock.getArtistByName(
                Mockito.any())).thenReturn(Mono.error(new IllegalArgumentException("Foo Bared")));

        webTestClient
                .get().uri(uriBuilder -> uriBuilder.path("/artist-release").queryParam("name", "hello").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("WHEN releaseRepository fails THEN expect failure")
    public void testReleaseWithDownStreamError2() {
        Mockito.when(artistRepositoryMock.getArtistByName(
                Mockito.any())).thenReturn(Mono.just(
                List.of(new Artist().setName("FooBaa").setCountry("AU").setDisambiguation("Made up artist").setArid("321"))
        ));
        Mockito.when(releaseRepositoryMock.getReleasesByArid(Mockito.any()))
                .thenReturn(Mono.error(new IllegalArgumentException("Foo Barred")));

        webTestClient
                .get().uri(uriBuilder -> uriBuilder.path("/artist-release").queryParam("name", "hello").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("WHEN with valid query parameter set AND artist, but empty release THEN expect ok and valid JSON")
    public void testReleaseWithQueryParam2() {
        Mockito.when(artistRepositoryMock.getArtistByName(
                Mockito.any())).thenReturn(Mono.just(
                List.of(new Artist().setName("FooBaa").setCountry("AU").setDisambiguation("Made up artist").setArid("321"))
        ));
        Mockito.when(releaseRepositoryMock.getReleasesByArid(Mockito.any()))
                .thenReturn(Mono.just(List.of()));

        webTestClient
                .get().uri(uriBuilder -> uriBuilder.path("/artist-release").queryParam("name", "arid").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.[*].country", "UK", "AU");
    }

    @Test
    @DisplayName("WHEN with valid query parameter set AND artist and release present THEN expect ok and valid JSON")
    public void testReleaseWithQueryParam3() {
        Mockito.when(artistRepositoryMock.getArtistByName(
                Mockito.any())).thenReturn(Mono.just(
                List.of(new Artist().setName("FooBaa").setCountry("AU").setDisambiguation("Made up artist").setArid("321"))
        ));
        Mockito.when(releaseRepositoryMock.getReleasesByArid(Mockito.any())).thenReturn(
                Mono.just(List.of(
                        new Release().setArtistName("FooBar2").setTitle("Doof doof").setCountry("UK"),
                        new Release().setArtistName("FooBar2").setTitle("Doof doof doof").setCountry("AU")
                ))
        );

        webTestClient
                .get().uri(uriBuilder -> uriBuilder.path("/artist-release").queryParam("name", "arid").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.[*].country", "UK", "AU");
    }
}
