package io.anuragksinha.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.anuragksinha.moviecatalogservice.models.CatalogItem;
import io.anuragksinha.moviecatalogservice.models.Movie;
import io.anuragksinha.moviecatalogservice.models.Rating;
import io.anuragksinha.moviecatalogservice.models.UserRating;
import io.anuragksinha.moviecatalogservice.services.MovieInfo;
import io.anuragksinha.moviecatalogservice.services.UserRatingInfo;

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
	@Autowired
	private MovieInfo movieInfo;
	@Autowired
	private UserRatingInfo userRatingInfo;

	@GetMapping("{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		UserRating ratings= userRatingInfo.getUserRating(userId);
		return ratings.getRatings().stream().map(rating -> movieInfo.getCatalogItem(rating))
				.collect(Collectors.toList());
	}
}
