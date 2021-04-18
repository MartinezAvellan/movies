package com.texo.movies.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ProducerDTO implements Comparable<ProducerDTO>, Serializable {

	private static final long serialVersionUID = 7762441038407734405L;

	private String produce;
	private Date year;

	@Override
	public int compareTo(final ProducerDTO p) {
		return getYear().compareTo(p.getYear());
	}

}