package com.texo.movies.interfaces;

import com.texo.movies.dto.GoldenRaspberryAwardsWinners;

public interface MoviesServiceInterface {
	
	public GoldenRaspberryAwardsWinners getWinnerLongerAndShorter() throws Exception;

}