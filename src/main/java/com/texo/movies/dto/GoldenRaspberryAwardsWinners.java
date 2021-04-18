package com.texo.movies.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class GoldenRaspberryAwardsWinners implements Serializable {

	private static final long serialVersionUID = -8509621128736936938L;
	
	private List<MoviesDTO> min;
	private List<MoviesDTO> max;

}