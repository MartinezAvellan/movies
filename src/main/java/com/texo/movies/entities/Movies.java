package com.texo.movies.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Movies")
public class Movies implements Serializable {
	
	private static final long serialVersionUID = -8248902102386774135L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 4)
	private String year;
	
	@Column(length = 250)
	private String title;
	
	@Column(length = 250)
	private String studios;
	
	@Column(length = 250)
	private String producers;
	
	private boolean winner;

}