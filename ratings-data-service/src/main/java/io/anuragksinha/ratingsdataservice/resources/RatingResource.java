package io.anuragksinha.ratingsdataservice.resources;

import java.util.Arrays;
import java.util.List;

import io.anuragksinha.ratingsdataservice.models.Rating;
import io.anuragksinha.ratingsdataservice.models.UserRating;
import org.apache.catalina.User;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratingsdata")
public class RatingResource {
	@RequestMapping("/{movieId}")
	public Rating getRating(@PathVariable("movieId") String movieId){
		return new Rating(movieId,4);
	}

	/**
	 * Returning a list of Object is not recommended as
	 * going forward due to some reason if you need to have a flag/property
	 * on a global level and not in the Object under the list then in that case
	 * this API needs to change and it will not support older consumers(will not be backward compatible)
	 * So even if you have only List<Object> then also its preferred to be wrapped around an object
	 * @param userId
	 * @return
	 */
	/*@RequestMapping("/users/{userId}")
	public List<Rating> getUserRating(@PathVariable("userId") String userId){
		return Arrays.asList(
				new Rating("1233", 5),
				new Rating("7736",4)
		);
	}*/
	@RequestMapping("/users/{userId}")
	public UserRating getUserRatings(@PathVariable("userId") String userId){
		UserRating userRating=new UserRating();
				userRating.setRatings(Arrays.asList(
				new Rating("550", 5),
				new Rating("551",4)
		));
		return userRating;
	}

}
