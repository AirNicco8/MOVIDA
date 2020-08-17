package src.niccolaibalica.Test;

import src.niccolaibalica.utils;

public class AVLtest {

    public static void main(String[] args) {

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
        printtg(u);
    }
}
