package com.texo.movies.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.texo.movies.dto.GoldenRaspberryAwardsWinners;
import com.texo.movies.interfaces.MoviesServiceInterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
public class MoviesController {
	
	@Autowired
	private MoviesServiceInterface moviesService;

	@GetMapping("/golden-raspberry-awards-winners")
	public @ResponseBody GoldenRaspberryAwardsWinners getGoldenRaspberryAwardsWinner() {
		GoldenRaspberryAwardsWinners retorno = new GoldenRaspberryAwardsWinners();
		try {
			retorno = moviesService.getWinnerLongerAndShorter();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return retorno;
	}
	
}