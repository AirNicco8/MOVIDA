package src.niccolaibalica.test;

import src.niccolaibalica.utils;
import src.niccolaibalica.dict.AvlTree;
import src.commons.Movie;

public class AVLtest {

    public static void main(String[] args) {
        utils y = new utils();
        Movie a,b,c,d;
        a = new Movie("Allah", 1998);

        b = new Movie("Hello molly", 1999);

        c = new Movie("Geiger", 2001);

        d = new Movie("Errand", 3444);

        AvlTree avl = new AvlTree(Movie.class);


        avl.insert(a.getTitle(), a);
        avl.insert(b.getTitle(), b);
        avl.insert(c.getTitle(), c);
        avl.insert(d.getTitle(), d);

        avl.printTree();

        System.out.println("ricerca");

        Movie u = (Movie)avl.search("Allah");
        y.printtg(u);
    }
}
