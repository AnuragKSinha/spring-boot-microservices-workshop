package io.anuragksinha.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.anuragksinha.moviecatalogservice.models.CatalogItem;
import io.anuragksinha.moviecatalogservice.models.Movie;
import io.anuragksinha.moviecatalogservice.models.Rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	/**
	 * Autowired is a consumer and Bean is producer,So
	 * its important that you have a Bean for a autowired variable
	 */
	@Autowired
	private RestTemplate restTemplate;
	@GetMapping("{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		List<Rating> ratings= Arrays.asList(
				new Rating("1233", 5),
				new Rating("7736",4)
		);
		return ratings.stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+ rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getName(),"Desc",rating.getRating());
		}).collect(Collectors.toList());
	}
}
