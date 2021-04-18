package com.texo.movies.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import com.texo.movies.entities.Movies;

/**
 * @author Hugo A. Martinez
 * @since 17/04/2021
 *
 */
@Component
@SuppressWarnings("unchecked")
public class MoviesDAO {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Movies> getAll() {
		final String QUERY = "SELECT m FROM Movies m";
		return entityManager.createQuery(QUERY).getResultList();
	}
	
	@Transactional
	public void insertMovies(final List<Movies> movies) {
		for (final Movies m : movies) {
			entityManager.persist(m);
			entityManager.flush();
		}
	}

}