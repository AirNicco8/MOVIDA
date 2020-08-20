package src.niccolaibalica.sort;

import src.commons.Movie;

import java.util.Comparator;

public class TitleSorter implements Comparator<Movie> {

    	@Override
    	public int compare(Movie o1, Movie o2) {
    		return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        /* RETURN 0 ---> stringhe uguali
          RETURN <0 ---> titolo di o1 minore lessicograficamente del titolo di o2
          RETURN >0 ---> titolo di o1 maggiore lessicograficamente del titolo di o2*/
    	}

}
