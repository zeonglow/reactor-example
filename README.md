# MusicBrainz Coding Assignment

* Christopher James Huang-Leaver
* March 29th 2021


## Design Brief

```
Create a http api application with at least one endpoint that when supplied with the
artist / band name will search the MusicBrainz.org service and return a response,
the return payload should be a collection of possible artist matches if there is more than one possible match,
or a collection of all the releases for an artist if there is a single artist with that name.
```

### Usage instructions

 The standard Gradle wrappers,  `gradlew.bat` for Windows, `./gradlew ` for others, are present and should 'just work' if you have Java installed correctly.

`gradle bootJar`

`java -jar build//libs/musicbrainz-0.0.1-SNAPSHOT.jar`

Or alternatively
`gradle bootRun`


### Initial thoughts and experiments

The first thing I did was to read the brief several times, out loud once, then had a play with the MusicBrainz API.
I just used CURL from the command line. From this I discover that MusicBrainz returns XML by default and JSON if you provide an 'accept' parameter.

This is an interesting challenge as straight away we have hit philosophy on Agile software development; it is apparently impossible to find an Artist name which only returns one match.

Are tickets contracts or are they invitations to conversations?

An endpoint which returns two different types of objects increases the complexity;  ignore Java static typing for a moment,  lets say I had implemented this in Python.  The API consumer has to either do some type of 'guess' to see which object it received, or we force always returning one type of object, i.e have a wrapper object,  along the lines of:

```
{
type="artist"
data=[...]
}
```

Perhaps if we narrowed the search criteria down; limit the search to Australian artists only say, maybe, it still seems unlikely to be that useful and the spec doesn't say that. It's easy to make code more complicated,  making it simple again is hard,  reclaiming all the time spent making it needlessly complex in the first place is impossible.

One thing I noticed while playing; The first artist returned by a simple search, at least for most of the artists I could think of, came top of the list, so implementing a "I feel lucky" type endpoint seemed like a good approach. It has a similar level of complexity for demonstration of coding skills yet is more useful. We also have endpoints for retrieval of all the artists and all the releases for a given artist id.


### Endpoints Exposed

* `"/artist-release?name=eels"`
Takes the first artist returned from the artist name search, use their artist id to search for releases, return those releases

* `"/artist?name=bonobo"`
Returns artists which match a given name

* `"/release?arid=ffabe-123..."`
Returns releases for a given arid, obtained from the artist endpoint or from MusicBrainz directly



### Design decisions and simplifications


#### Technology Choices

* Gradle
* SpringBoot
* WebFlux
* AssertJ and UnitTest5

My first choice,  which jumped at me while reading the brief, was to use Asynchronous IO; an 'adapter' style microservice is going to be mostly bound by IO. The Python aiohttp library would have probably worked well for this, but this is Java role, so that was out.

*Gradle*  I've used it before, it doesn't use XML for configuration, so is easier to review and spot bugs.

*SpringBoot*  It's an industry standard, it was mentioned you use this already, it also simplifies this type of code.


#### Limitations

* Use of Mono<> not Flux<>

Retrieving a stream of data only works if the remote server supports JSON streaming,  which MusicBrainz does not at the time of writing. This means we have to download the whole request to memory before we can process it. We can still process asynchronously, MusicBrainz paginates to batches of 25 by default.

* Ignoring pagination

This application only returns the first 25 records, it ignores all the other data. With the Artist endpoint this is reasonable,  the first artist returned was always the one I wanted, although your millage my vary.  With the releases endpoint it was simply real-life syndrome as well as the keep the simple enough to review.

There are also design questions around pagination,  how much latency is acceptable?  Do we want a streaming endpoint? Does the client get to choose how many requests they want,  (which implies my microservice needs to handle pagination and offsets somehow)


* Downstream endpoints

MusicBrainz appears to have some type of strict request limiting,  which is why I disabled the tests which call the API once I was happy they worked.

* Ignores missing accept parameter

We serve the user JSON if they do not specify an accept parameter. Thanks to SpringBoot magic we do return a 406 if you insist on something other than JSON

* Error Handling

 I use the webflux facilities to handle the 'error' state of Mono/Flux objects. We only get verbose backtraces in error messages if you run the code from within an IDE,  running the JAR removes this, thanks to the magic of `developmentOnly 'org.springframework.boot:spring-boot-devtools'` in the Gradle file.
