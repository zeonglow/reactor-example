package au.co.huangleaver.chris.musicbrainz.controllers;

import au.co.huangleaver.chris.musicbrainz.model.Artist;
import au.co.huangleaver.chris.musicbrainz.model.Release;
import au.co.huangleaver.chris.musicbrainz.repository.ArtistRepository;
import au.co.huangleaver.chris.musicbrainz.repository.ReleaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
public class MusicController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ReleaseRepository releaseRepository;

    @GetMapping(path = "/artist-release", params = {"name"})
    public Mono<List<Release>> artistAndRelease(@RequestParam final String name) {
        /*
          Returns all the releases from the highest score match for artist search

          Actually not quite true,  it returns the first 25 releases due to pagination not being implemented.

          The spec says "Return all the releases if only one artist is returned". That *never* happens. (I haven't
          tried every Artist in MusicBrainz, but it seems to be pretty much impossible given how Elasticsearch works)

          See README.md for details
         */
        Mono<List<Artist>> artistListMono = artistRepository.getArtistByName(name);

        return artistListMono.flatMap(
                artists -> {
                    if (artists == null || artists.isEmpty()) {
                        return Mono.empty();
                    }
                    return releaseRepository.getReleasesByArid(artists.get(0).getArid());
                }
        );
    }

    @GetMapping(path = "/artist", params = {"name"})
    public Mono<List<Artist>> artistRoute(@RequestParam final String name) {
        return artistRepository.getArtistByName(name);
    }

    @GetMapping(path = "/release", params = {"arid"})
    public Mono<List<Release>> releaseRoute(@RequestParam final String arid) {
        return releaseRepository.getReleasesByArid(arid);
    }


}

