package src.niccolaibalica.Sort;

import src.commons.Movie;

import java.util.Comparator;

public class TitleSorter implements Comparator<Movie> {

    	@Override
    	public int compare(Movie o1, Movie o2) {
    		return o1.getTitle().compareToIgnoreCase(o2.getTitle());
    	}

}
