package io.anuragksinha.movieinfoservice.resources;

import java.time.Duration;

import io.anuragksinha.movieinfoservice.models.Movie;
import io.anuragksinha.movieinfoservice.models.MovieSummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/movies")
public class MovieResource {
	@Autowired
	private WebClient.Builder webClient;

	@Value("${api.key}")
	private String apiKey;

	@RequestMapping("/{movieId}")
	public Movie getMovieInfo(@PathVariable("movieId") String movieId){
		MovieSummary movieSummary = webClient.build()
				.get()
				.uri("https://api.themoviedb.org/3/movie/"+movieId+"?api_key="+apiKey)
				.retrieve()
				.bodyToMono(MovieSummary.class)
				.timeout(Duration.ofSeconds(1)) // Added request level timeout
				.block();
		return new Movie(movieId,movieSummary.getOverview(),movieSummary.getTitle());
	}
}
