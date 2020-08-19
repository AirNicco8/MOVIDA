package src.niccolaibalica;

import java.io.*;

public class MovidaCoreTest {
    public static void main(String[] args) {
        MovidaCore mc = new MovidaCore();
        String path = "../commons/esempio-formato-dati.txt";
        mc.loadFromFile(new File(path));
    }
}
