package io.anuragksinha.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.anuragksinha.moviecatalogservice.models.CatalogItem;
import io.anuragksinha.moviecatalogservice.models.Movie;
import io.anuragksinha.moviecatalogservice.models.Rating;
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
				.uri("http://localhost:8083/ratingsdata/users/"+ userId)
				.retrieve()
				.bodyToMono(UserRating.class)
				.block();
		return ratings.getRatings().stream().map(rating -> {
			Movie movie = webClient.build()
					.get()
					.uri( "http://localhost:8082/movies/"+ rating.getMovieId())
					.retrieve()
				/**
				* The movie class should have a default constructor if not then 
				* Exception will be thrown
				*/
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
			return new CatalogItem(movie.getName(),"Desc",rating.getRating());
		}).collect(Collectors.toList());
	}
}
