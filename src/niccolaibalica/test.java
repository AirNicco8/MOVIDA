package src.niccolaibalica;


public class test {

    public class Movie {

        private String title;
        private Integer year;

        public Movie(String title, Integer year) {
            this.title = title;
            this.year = year;
        }
    }

    public void main(String[] args) {

        Movie a;
        a = new Movie("test", 1);

        avlTree avl = new avlTree(Movie.class);


        avl.insert("a", a);
        avl.insert("b", a);
        avl.insert("c", a);
        avl.insert("d", a);

        avl.delete("b");

        avl.traverse();

        System.out.println("=== new Tree ===");
    }
}