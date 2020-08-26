package src.niccolaibalica;

import src.commons.MovidaFileException;
import src.commons.Movie;
import src.commons.Person;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

public class utils<V> {

    public String printtg(Movie m) {
      return (m.getTitle() + "!" + m.getYear().toString());
    }

    public<V> void swap(V[] array, int i, int j) {
    	V temp = array[i];
    	array[i] = array[j];
    	array[j] = temp;
    }

    public Movie[] extFilms(File f){
        String[] lineFilm = new String[5];
        ArrayList<Movie> allFilms = new ArrayList<>();

        try {
             Scanner input = new Scanner(f);
                while(input.hasNextLine()) {
                    for(int j = 0; j < 5; j++) {
                        String line = input.nextLine();
                        if (!line.matches("(.*):(.*)")){
                            throw new MovidaFileException();
                        }
                        lineFilm[j] = line;
                        System.out.println(line);
                    }


                String[] filmFields = this.getValues(lineFilm); //array di 5 valori che sono i 5 campi dell'oggetto movie

                Movie newFilm = this.stringToMovie(filmFields); //riempo il record coi valori salvati nell'array

                allFilms.add(newFilm);                        //aggiungo alla lista di Movie il contenuto del film (di tipo Movie) estratto

                    if (input.hasNextLine()) {
                        if (!input.nextLine().trim().isEmpty()) {
                            throw new MovidaFileException();
                        }
                    }
          }
        } catch (Exception m){
            System.out.println(new MovidaFileException().getMessage());
            m.printStackTrace();
        }
        Movie[] mov = new Movie[allFilms.size()];
        return allFilms.toArray(mov);
      }

      public String[] getValues(String[] f){
          String[] filmValues = new String[5];
          for(int i=0;i<5;i++) {
              String[] s = f[i].split(":");
              filmValues[i] = s[1];
              System.out.println(filmValues[i]);
          }
          return filmValues;
      }


      public Movie stringToMovie(String[] f){
          String title = f[0].trim();
          Integer year = Integer.parseInt(f[1].trim());
          Person director = new Person(f[2].trim());
          String[] castM = f[3].trim().split(",");
          Person[] cast = this.getCast(castM);
          Integer votes = Integer.parseInt(f[4].trim());
          return new Movie(title,year,votes,cast,director);
      }

      public Person[] getCast(String[] names){
          Person[] cast = new Person[names.length];
          for(int i = 0; i < names.length; i++){
              cast[i] = new Person(names[i].trim());
          }
          return cast;
      }
}
