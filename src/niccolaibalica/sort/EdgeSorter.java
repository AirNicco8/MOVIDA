package src.niccolaibalica.sort;

import src.commons.Collaboration;
import src.niccolaibalica.asdlab.Arco;

import java.util.Comparator;

public class EdgeSorter implements Comparator<Arco> {

      @Override
      public int compare(Arco a1, Arco a2) {
        Collaboration c1 = (Collaboration) a1.info;
        Collaboration c2 = (Collaboration) a2.info;
        return c1.getScore().compareTo(c2.getScore());
      }
      /*
      - return 0 ---> score di a1 = score di a2;
      - return <0 ---> score di a1 è minore di score di a2 (peggio)
      - return >0 ---> score di a1 è maggiore di a2 (meglio)
      */

}
