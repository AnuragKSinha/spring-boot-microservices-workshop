package io.anuragksinha.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.anuragksinha.moviecatalogservice.models.CatalogItem;
import io.anuragksinha.moviecatalogservice.models.Movie;
import io.anuragksinha.moviecatalogservice.models.Rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MovieInfo {
	@Autowired
	private WebClient.Builder webClient;

	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
	public CatalogItem getCatalogItem(Rating rating){
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
	}
	public CatalogItem getFallbackCatalogItem(Rating rating){
		return new CatalogItem("No movie","",rating.getRating());
	}
}
