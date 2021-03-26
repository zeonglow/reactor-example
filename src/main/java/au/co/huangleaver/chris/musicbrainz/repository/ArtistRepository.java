package au.co.huangleaver.chris.musicbrainz.repository;

import au.co.huangleaver.chris.musicbrainz.model.Artist;
import au.co.huangleaver.chris.musicbrainz.model.ArtistQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class ArtistRepository {
    private static final String baseUrl = "http://musicbrainz.org/ws/2/artist/";
    private static final WebClient artistWebClient = WebClient.create(baseUrl);
    private static final Logger log = LoggerFactory.getLogger(ArtistRepository.class);

    public Mono<List<Artist>> getArtistByName(String name) {
        return artistWebClient.get()
                /* The query is being assembled by UriBuilder, so it's fine 😉 */
                .uri(uriBuilder -> uriBuilder.query(String.format("query=artist:%s", name)).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ArtistQueryResponse.class)
                .flatMap(artistQueryResponse -> Mono.just(artistQueryResponse.getArtists()))
                .doOnError(e -> log.error("Downstream API error:", e));
    }
}
