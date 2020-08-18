package src.niccolaibalica.Sort;

import src.commons.Movie;

import java.util.Comparator;

public class RatingSorter implements Comparator<Movie> {

      @Override
      public int compare(Movie o1, Movie o2) {
        return o1.getVotes() - o2.getVotes();
      }

}
