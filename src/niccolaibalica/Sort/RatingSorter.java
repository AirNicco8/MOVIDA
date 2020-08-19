package src.niccolaibalica.sort;

import src.commons.Movie;

import java.util.Comparator;

public class RatingSorter implements Comparator<Movie> {

      @Override
      public int compare(Movie o1, Movie o2) {
        return o1.getVotes() - o2.getVotes();
        /* -return >0 ---> o1 ha più voti
           -return <0 ---> o1 ha meno voti
        */
      }

}
