package src.niccolaibalica.sort;

import src.commons.Movie;

import java.util.Comparator;


public class TitleSorter implements Comparator<Movie> {

	@Override
	public int compare(Movie o1, Movie o2) {
		return o1.getTitle().compareToIgnoreCase(o2.getTitle());
	}

}

public class RatingSorter implements Comparator<Movie> {

	@Override
	public int compare(Movie o1, Movie o2) {
		return o1.getVotes() - o2.getVotes();
	}

}

public class AgeSorter implements Comparator<Movie> {

	@Override
	public int compare(Movie o1, Movie o2) {
		return o1.getYear().compareTo(o2.getYear());
	}
}
