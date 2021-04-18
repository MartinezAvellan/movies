package com.texo.movies.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MoviesDTO implements Serializable {

	private static final long serialVersionUID = -6936968551951080776L;
	
	private String producer;
	private int interval;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy") 
	private Date previousWin;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy") 
	private Date followingWin;

}