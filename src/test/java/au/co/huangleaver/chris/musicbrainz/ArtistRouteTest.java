package au.co.huangleaver.chris.musicbrainz;

import au.co.huangleaver.chris.musicbrainz.model.Artist;
import au.co.huangleaver.chris.musicbrainz.repository.ArtistRepository;
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
public class ArtistRouteTest {

    @MockBean
    ArtistRepository artistRepositoryMock;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    private void resetMock() {
        Mockito.reset(artistRepositoryMock);
    }

    @Test
    @DisplayName("WHEN no query parameter set THEN expect BadRequest")
    public void testArtist() {
        webTestClient
                .get().uri("/artist")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("WHEN with valid query parameter set AND empty response THEN expect ok and vaild JSON")
    public void testArtistWithQuery1() {
        Mockito.when(artistRepositoryMock.getArtistByName(Mockito.any())).thenReturn(Mono.empty());
        webTestClient
                .get().uri(uriBuilder -> uriBuilder.path("/artist").queryParam("name", "moby").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$", "");
    }

    @Test
    @DisplayName("WHEN with valid query parameter set AND one artist response THEN expect ok")
    public void testArtistWithQuery2() {
        Mockito.when(artistRepositoryMock.getArtistByName(Mockito.any())).thenReturn(Mono.just(
                List.of(new Artist().setName("FooBaa").setCountry("AU").setDisambiguation("Made up artist").setArid("321"))
        ));

        webTestClient
                .get().uri(uriBuilder -> uriBuilder.path("/artist").queryParam("name", "moby").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].name", "FooBaa");
    }
}
