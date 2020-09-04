package src.niccolaibalica.sort;

import src.commons.Movie;

import java.util.Comparator;

public class YearSorter implements Comparator<Movie> {

	@Override
	public int compare(Movie o1, Movie o2) {
		return o1.getYear().compareTo(o2.getYear());
	}
	/*
	 * - return 0 ---> anno di o1 = anno di o2;
	 * - return <0 ---> anno di o1 è minore di anno di o2 (più vecchio)
	 * - return >0 ---> anno di o1 è maggiore di o2 (più recente)
	 */

}
