package src.niccolaibalica;

import java.util.Arrays;
import java.io.*;
import src.commons.MapImplementation;

public class MovidaCoreTest {
    public static void main(String[] args){
        MovidaCore mc = new MovidaCore();
        String path = "esempio-formato-dati.txt";

        if (!new File(path).exists())
          {
             System.out.println("Unvalid path");
          }

        mc.loadFromFile(new File(path));
        mc.printDicts();
    }
}
