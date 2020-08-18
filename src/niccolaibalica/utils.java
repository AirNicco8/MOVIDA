package src.niccolaibalica;

import src.commons.MovidaFileException;
import src.commons.Movie;
import src.commons.Person;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class utils {

    public String printtg(Movie m) {
      return (m.getTitle() + "!" + m.getYear().toString());
    }

    public void swap(V[] array, int i, int j) {
    	V temp = array[i];
    	array[i] = array[j];
    	array[j] = temp;
    }
}
