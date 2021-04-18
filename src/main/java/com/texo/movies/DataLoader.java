package com.texo.movies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.texo.movies.dao.MoviesDAO;
import com.texo.movies.entities.Movies;
import com.texo.movies.services.MoviesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("!test")
public class DataLoader implements ApplicationRunner {

	@Autowired
	private MoviesDAO moviesDAO;

    public void run(ApplicationArguments args) throws Exception {
    	this.readFileAndInsert();
    }

	private void readFileAndInsert() throws Exception {
		final List<Movies> movies = new ArrayList<>();
		final URL resource = MoviesService.class.getClassLoader().getResource("movielist.csv");
		try (BufferedReader br = new BufferedReader(new FileReader(new File(resource.toURI())))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.contains("year;title;studios;producers;winner")) {
					String[] columns = line.split(";");
					final Movies movie = new Movies();
					movie.setYear(String.valueOf(columns[0]));
					movie.setTitle(String.valueOf(columns[1]));
					movie.setStudios(String.valueOf(columns[2]));
					movie.setProducers(String.valueOf(columns[3]));
					if (columns.length == 5) {
						movie.setWinner(String.valueOf(columns[4]).equalsIgnoreCase("yes") ? true : false);
					}
					movies.add(movie);
				}
			}
			moviesDAO.insertMovies(movies);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
}
