package com.texo.movies;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.texo.movies.dao.MoviesDAO;
import com.texo.movies.dto.GoldenRaspberryAwardsWinners;
import com.texo.movies.dto.MoviesDTO;
import com.texo.movies.entities.Movies;
import com.texo.movies.interfaces.MoviesServiceInterface;

@SpringBootTest
@ActiveProfiles("test")
class MoviesTest {
	
	@Autowired
	private MoviesDAO moviesDAO;
	
	@Autowired
	private MoviesServiceInterface moviesService;

	@BeforeEach
	void init() throws Exception {
		final List<Movies> movies = new ArrayList<>();
		final URL resource = MoviesTest.class.getClassLoader().getResource("teste.csv");
		try (BufferedReader br = new BufferedReader(new FileReader(new File(resource.toURI())))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if (!line.contains("year;title;studios;producers;winner")) {
		    		final String[] columns = line.split(";");
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
			throw e;
		}
	}
	
	@Test
	void winnerShorterInterval() throws Exception {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		
		final GoldenRaspberryAwardsWinners winnerLongerAndShorter = moviesService.getWinnerLongerAndShorter();
		assertNotNull("OBJETO GoldenRaspberryAwardsWinners NAO PODE SER NULL", winnerLongerAndShorter);
		
		final List<MoviesDTO> winnerShorterInterval = winnerLongerAndShorter.getMin();
		assertTrue(winnerShorterInterval.size() == 1, "DEVE CONTER UM ELEMENTO APENAS E NAO " + winnerShorterInterval.size());
		assertTrue(winnerShorterInterval.get(0).getProducer().equals("Frederico Silva"), "O PRODUCER DEVE SER Frederico Silva E NAO " + winnerShorterInterval.get(0).getProducer());
		assertTrue(winnerShorterInterval.get(0).getInterval() == 1, "O INTERVAL DEVE SER 1 anos E NAO " + winnerShorterInterval.get(0).getInterval());
		assertTrue(winnerShorterInterval.get(0).getPreviousWin().compareTo(sdf.parse("1990")) == 0, "O PREVIOUS WIN DEVE SER 1990 E NAO " + winnerShorterInterval.get(0).getPreviousWin());
		assertTrue(winnerShorterInterval.get(0).getFollowingWin().compareTo(sdf.parse("1991")) == 0, "O FOLLOWING WIN DEVE SER 1991 E NAO " + winnerShorterInterval.get(0).getFollowingWin());

		final List<MoviesDTO> winnerLongerInterval = winnerLongerAndShorter.getMax();
		assertTrue(winnerLongerInterval.size() == 1, "DEVE CONTER UM ELEMENTO APENAS E NAO " + winnerLongerInterval.size());
		assertTrue(winnerLongerInterval.get(0).getProducer().equals("Jose Matheus"), "O PRODUCER DEVE SER Jose Matheus E NAO " + winnerLongerInterval.get(0).getProducer());
		assertTrue(winnerLongerInterval.get(0).getInterval() == 28, "O INTERVAL DEVE SER 28 anos E NAO " + winnerLongerInterval.get(0).getInterval());
		assertTrue(winnerLongerInterval.get(0).getPreviousWin().compareTo(sdf.parse("1993")) == 0, "O PREVIOUS WIN DEVE SER 1993 E NAO " + winnerLongerInterval.get(0).getPreviousWin());
		assertTrue(winnerLongerInterval.get(0).getFollowingWin().compareTo(sdf.parse("2021")) == 0, "O FOLLOWING WIN DEVE SER 2021 E NAO " + winnerLongerInterval.get(0).getFollowingWin());
	}

}
