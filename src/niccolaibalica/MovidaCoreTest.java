package src.niccolaibalica;

import java.util.Arrays;
import java.io.*;
import src.commons.*;

public class MovidaCoreTest {
    public static void main(String[] args){
        MovidaCore mc = new MovidaCore();
        // user.dir => bin
        String path = "esempio-formato-dati.txt";
        Movie[] arr = new Movie[10];
        Person[] arrp = new Person[10];

        mc.loadFromFile(mc.toDBfile(path));
        mc.printDicts();
        System.out.println("");
        System.out.println("%%%%%%% test searchMoviesByTitle %%%%%%%%%");

        arr = mc.searchMoviesByTitle("The");
          for(Movie m : arr)
            System.out.println(m.toString());

        System.out.println("");

        arr = mc.searchMoviesByTitle("Pulp");
          for(Movie m : arr)
            System.out.println(m.toString());

        System.out.println("");
        System.out.println("%%%%%%% test searchMoviesInYear %%%%%%%%%");

        arr = mc.searchMoviesInYear(1997);
          for(Movie m : arr){
              String str = String.valueOf(m.getYear());
              System.out.println(m.toString() + "  " + str);
          }

        System.out.println("");

        arr = mc.searchMoviesInYear(1999);
            for(Movie m : arr){
                String str = String.valueOf(m.getYear());
                System.out.println(m.toString() + "  " + str);
            }

        System.out.println("");

        arr = mc.searchMoviesInYear(2345);
            for(Movie m : arr){
                  String str = String.valueOf(m.getYear());
                  System.out.println(m.toString() + "  " + str);
            }

        System.out.println("");
        System.out.println("%%%%%%% test searchMostVotedMovies %%%%%%%%%");

        arr = mc.searchMostVotedMovies(5);
            for(Movie m : arr){
                String str = String.valueOf(m.getVotes());
                System.out.println(m.toString() + "  " + str);
            }

        System.out.println("");

        arr = mc.searchMostVotedMovies(9);
            for(Movie m : arr){
                String str = String.valueOf(m.getVotes());
                System.out.println(m.toString() + "  " + str);
            }

        System.out.println("");
        System.out.println("%%%%%%% test searchMostRecentMovies %%%%%%%%%");

        arr = mc.searchMostRecentMovies(5);
            for(Movie m : arr){
                String str = String.valueOf(m.getYear());
                System.out.println(m.toString() + "  " + str);
            }

        System.out.println("");

        arr = mc.searchMostRecentMovies(9);
            for(Movie m : arr){
                String str = String.valueOf(m.getYear());
                System.out.println(m.toString() + "  " + str);
            }

        System.out.println("");
        System.out.println("%%%%%%% test searchMoviesDirectedBy %%%%%%%%%");

        arr = mc.searchMoviesDirectedBy("Martin Scorsese");
            for(Movie m : arr){
                String str = m.getDirector().getName();
                System.out.println(m.toString() + "  " + str);
            }

        System.out.println("");

        arr = mc.searchMoviesDirectedBy("Brian De Palma");
            for(Movie m : arr){
                String str = m.getDirector().getName();
                System.out.println(m.toString() + "  " + str);
            }

        System.out.println("");
        System.out.println("%%%%%%% test searchMoviesStarredBy %%%%%%%%%");

        arr = mc.searchMoviesStarredBy("Robert De Niro");
            for(Movie m : arr){
                Person[] ps = m.getCast();
                System.out.println(m.toString());
                for(Person p : ps)
                  System.out.println(p.toString());
            }

        System.out.println("");

        arr = mc.searchMoviesStarredBy("Harrison Ford");
            for(Movie m : arr){
                Person[] ps = m.getCast();
                System.out.println(m.toString());
                for(Person p : ps)
                  System.out.println(p.toString());
            }

        System.out.println("");
        System.out.println("%%%%%%% test searchMostActiveActors %%%%%%%%%");

        arrp = mc.searchMostActiveActors(5);

            for(Person p : arrp){
                arr = mc.searchMoviesStarredBy(p.getName());
                String str = String.valueOf(arr.length);
                System.out.println(p.toString()+" numero film:"+str);
            }

        System.out.println("");

        arrp = mc.searchMostActiveActors(9);
            for(Person p : arrp){
              arr = mc.searchMoviesStarredBy(p.getName());
              String str = String.valueOf(arr.length);
              System.out.println(p.toString()+" numero film:"+str);
            }

        System.out.println("");
        System.out.println("%%%%%%% test getDirectCollaboratorsOf %%%%%%%%%");

    }
}
