package io.anuragksinha.moviecatalogservice.models;

public class Movie {
	private String movieId;
	private String overview;
	private String title;

	public Movie() {
	}

	public Movie(String movieId, String overview,String title) {
		this.movieId = movieId;
		this.overview = overview;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}
}
