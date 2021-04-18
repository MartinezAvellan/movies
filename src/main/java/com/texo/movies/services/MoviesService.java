package com.texo.movies.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.texo.movies.dao.MoviesDAO;
import com.texo.movies.dto.GoldenRaspberryAwardsWinners;
import com.texo.movies.dto.MoviesDTO;
import com.texo.movies.dto.ProducerDTO;
import com.texo.movies.entities.Movies;
import com.texo.movies.interfaces.MoviesServiceInterface;

@Service
public class MoviesService implements MoviesServiceInterface {
	
	@Autowired
	private MoviesDAO moviesDAO;
	
	private HashMap<String, List<Date>> getOnlyWinners() throws Exception {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		final List<ProducerDTO> producers = new ArrayList<>();
		final List<Movies> listMovies = moviesDAO.getAll();
		for (final Movies movies : listMovies) {
			if (movies.isWinner()) {
				final String[] producersSplit = movies.getProducers().split(",");
				for (final String p : producersSplit) {
					if (p.contains(" and ")) {
						final String[] newSplit = p.split(" and ");
						for (final String nSp : newSplit) {
							if(nSp.length() > 1) {
								final ProducerDTO producer = new ProducerDTO();
								producer.setProduce(nSp.trim());
								producer.setYear(sdf.parse(movies.getYear()));
								producers.add(producer);
							}
						}
					} else {
						if(p.length() > 1) {
							final ProducerDTO producer = new ProducerDTO();
							producer.setProduce(p.trim());
							producer.setYear(sdf.parse(movies.getYear()));
							producers.add(producer);
						}
					}
				}
			}
		}
		Collections.sort(producers);
		final HashMap<String, List<Date>> hashMap = new HashMap<>();
		for (final ProducerDTO producer : producers) {
			if (!hashMap.containsKey(producer.getProduce())) {
				List<Date> list = new ArrayList<>();
				list.add(producer.getYear());
				hashMap.put(producer.getProduce(), list);
			} else {
				hashMap.get(producer.getProduce()).add(producer.getYear());
			}
		}
		return hashMap;
	}
	
	private int getDiffYears(Date first, Date last) {
	    Calendar a = getCalendar(first);
	    Calendar b = getCalendar(last);
	    int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
	    return diff;
	}

	private Calendar getCalendar(Date date) {
	    final Calendar cal = Calendar.getInstance(Locale.US);
	    cal.setTime(date);
	    return cal;
	}
	
	public GoldenRaspberryAwardsWinners getWinnerLongerAndShorter() throws Exception {
		final GoldenRaspberryAwardsWinners retorno = new GoldenRaspberryAwardsWinners();
		
		final HashMap<String, List<Date>> onlyWinners = this.getOnlyWinners();
		retorno.setMin(winnerShorterInterval(onlyWinners));
		retorno.setMax(winnerLongerInterval(onlyWinners));
		
		return retorno;
	}

	private List<MoviesDTO> winnerShorterInterval(final HashMap<String, List<Date>> onlyWinners) throws Exception {
		final List<MoviesDTO> list = new ArrayList<>();
		onlyWinners.forEach((key, value) -> {
			if (value.size() > 1) {
				final int diffYears = this.getDiffYears(value.get(0), value.get(1));
				if (diffYears == 1) {
					final MoviesDTO movie = new MoviesDTO();
					movie.setProducer(key);
					movie.setInterval(diffYears);
					movie.setPreviousWin(value.get(0));
					movie.setFollowingWin(value.get(1));
					list.add(movie);
				}
			}
		});
		return list;
	}
	
	private List<MoviesDTO> winnerLongerInterval(final HashMap<String, List<Date>> onlyWinners) throws Exception {
		final List<MoviesDTO> list = new ArrayList<>();
		onlyWinners.forEach((key, value) -> {
			if (value.size() > 1) {
				final int diffYears = this.getDiffYears(value.get(0), value.get(value.size() - 1));
				if (list.size() >= 1) {
					for (final MoviesDTO m : list) {
						if (m.getInterval() == diffYears) {
							final MoviesDTO movie = new MoviesDTO();
							movie.setProducer(key);
							movie.setInterval(diffYears);
							movie.setPreviousWin(value.get(0));
							movie.setFollowingWin(value.get(1));
							list.add(movie);
						} else if (m.getInterval() < diffYears) {
							for (int i = 0; i < list.size(); i++) {
								list.remove(i);
							} 
							final MoviesDTO movie = new MoviesDTO();
							movie.setProducer(key);
							movie.setInterval(diffYears);
							movie.setPreviousWin(value.get(0));
							movie.setFollowingWin(value.get(1));
							list.add(movie);
						}
					}
				} else {
					final MoviesDTO movie = new MoviesDTO();
					movie.setProducer(key);
					movie.setInterval(diffYears);
					movie.setPreviousWin(value.get(0));
					movie.setFollowingWin(value.get(1));
					list.add(movie);
				}
			}
		});

		return list;
	}

}	