package au.co.huangleaver.chris.musicbrainz.repository;

import au.co.huangleaver.chris.musicbrainz.model.Release;
import au.co.huangleaver.chris.musicbrainz.model.ReleaseQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class ReleaseRepository {

    private static final String baseUrl = "http://musicbrainz.org/ws/2/release/";
    private static final WebClient releaseClient = WebClient.create(baseUrl);
    private static final Logger log = LoggerFactory.getLogger(ReleaseRepository.class);

    public Mono<List<Release>> getReleasesByArid(String arid) {
        return releaseClient.get()
                /* The query is being assembled by UriBuilder, so it's fine 😉 */
                .uri(uriBuilder -> uriBuilder.query(String.format("query=arid:%s", arid)).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ReleaseQueryResponse.class)
                .flatMap(releaseQueryResponse -> Mono.just(releaseQueryResponse.getReleases()))
                .doOnError(e -> log.error("Downstream API error:", e));
    }
}
