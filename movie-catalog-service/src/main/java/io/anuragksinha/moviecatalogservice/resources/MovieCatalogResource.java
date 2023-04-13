package io.anuragksinha.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import io.anuragksinha.moviecatalogservice.models.CatalogItem;
import io.anuragksinha.moviecatalogservice.models.Movie;
import io.anuragksinha.moviecatalogservice.models.UserRating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	/**
	 * Autowired is a consumer and Bean is producer,So
	 * its important that you have a Bean for a autowired variable
	 */
	@Autowired
	private WebClient.Builder webClient;
	@GetMapping("{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		UserRating ratings= webClient.build()
				.get()
				.uri("http://ratings-data-service/ratingsdata/users/"+ userId)
				.retrieve()
				.bodyToMono(UserRating.class)
				.block();
		return ratings.getRatings().stream().map(rating -> {
			Movie movie = webClient.build()
					.get()
					.uri( "http://movie-info-service/movies/"+ rating.getMovieId())
					.retrieve()
					.bodyToMono(Movie.class)
			/**
			 * mono --> is a reactive way of saying that
			 * eventually you will get an object of this type
			 */
					.block();
			/**
			 * blocking the execution till the
			 * mono is fulfilled
			 */
			return new CatalogItem(movie.getTitle(), movie.getOverview(), rating.getRating());
		}).collect(Collectors.toList());
	}
}
