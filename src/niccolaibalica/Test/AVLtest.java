package src.niccolaibalica;


public class AVLtest {

    public static class Movie {

        private String title;
        private Integer year;

        public Movie(String title, Integer year) {
            this.title = title;
            this.year = year;
        }

        public String getTitle(){
          return title;
        }

        public void printtg() {
          System.out.println(title + "!" + year.toString());
        }
    }

    public static void main(String[] args) {

        Movie a,b,c,d;
        a = new Movie("Allah", 1998);

        b = new Movie("Hello molly", 1999);

        c = new Movie("Geiger", 2001);

        d = new Movie("Errand", 3444);

        avlTree avl = new avlTree(Movie.class);


        avl.insert(a.getTitle(), a);
        avl.insert(b.getTitle(), b);
        avl.insert(c.getTitle(), c);
        avl.insert(d.getTitle(), d);

        avl.printTree();

        System.out.println("ricerca");

        Movie u = (Movie)avl.search("Allah");
        u.printtg();
    }
}
