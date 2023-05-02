package io.anuragksinha.moviecatalogservice.services;

import java.util.Arrays;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.anuragksinha.moviecatalogservice.models.Rating;
import io.anuragksinha.moviecatalogservice.models.UserRating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserRatingInfo {
	@Autowired
	private WebClient.Builder webClient;

	@HystrixCommand(fallbackMethod = "getFallUserRating")
	public UserRating getUserRating(String userId){
		return webClient.build()
				.get()
				.uri("http://ratings-data-service/ratingsdata/users/"+ userId)
				.retrieve()
				.bodyToMono(UserRating.class)
				.block();
	}
	public UserRating getFallUserRating(String userId){
		UserRating userRating= new UserRating();
		userRating.setUserId(userId);
		userRating.setRatings(Arrays.asList(new Rating("0",0)));
		return userRating;
	}
}
