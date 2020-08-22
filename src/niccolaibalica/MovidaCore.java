package src.niccolaibalica;

import src.niccolaibalica.asdlab.*;
import src.niccolaibalica.dict.*;
import src.niccolaibalica.sort.*;
import src.commons.*;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;



public class MovidaCore implements IMovidaSearch,IMovidaConfig,IMovidaDB,IMovidaCollaborations {
    private utils db_utils;
    SortingAlgorithm sort;
    MapImplementation map;
    Dictionary<Movie> movies;
    Dictionary<Person> people;
    GrafoLA collabs; // struttura Collaborations, nodi = attori & archi = Collaboration

    public MovidaCore() {
        // TODO debugging / default values
        this.sort = SortingAlgorithm.SelectionSort;
        this.map = MapImplementation.AVL;
        this.movies = null;
        this.people = null;
        this.collabs = null;
    }

/** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DELLA CONFIG $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/


    protected<V> Dictionary<V> createDizionario(Class<V> c) {
        if (map == MapImplementation.AVL)
            return new AvlTree<V>(c);
        else if (map == MapImplementation.HashConcatenamento)
            return new HashCon<V>(42, c);   //TODO replace 42 with valid value
        return null;
    }

    @Override
    public boolean setSort(SortingAlgorithm a) {
        if (a != sort) {
            if (a == SortingAlgorithm.SelectionSort || a == SortingAlgorithm.MergeSort) {
                sort = a;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setMap(MapImplementation m) {
       boolean rit = false;
       if (m != map) {
           if (m == MapImplementation.AVL || m == MapImplementation.HashConcatenamento) {
               map = m; // Modifichiamo il tipo di dizionario usato
               if( movies != null && people != null){
                   Dictionary<Person> newPeople = createDizionario(Person.class);
                   Dictionary<Movie> newMovies = createDizionario(Movie.class);
                   for (Movie movie : getAllMovies()) { // (!) metodo che verrà implementato nella parte db
                       newMovies.insert(movie.getTitle(), movie);
                   }
                   for (Person person : getAllPeople()) {
                       newPeople.insert(person.getName(), person);
                   }
                   movies = newMovies;
                   people = newPeople;
                   rit = true;
               }
           }
       }
       return rit;
   }


    /** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DEL DB $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/

    /**
     * Carica i dati da un file, organizzato secondo il formato MOVIDA (vedi esempio-formato-dati.txt)
     *
     * Un film è identificato in modo univoco dal titolo (case-insensitive), una persona dal nome (case-insensitive).
     * Semplificazione: non sono gestiti omonimi e film con lo stesso titolo.
     *
     * I nuovi dati sono aggiunti a quelli già caricati.
     *
     * Se esiste un film con lo stesso titolo il record viene sovrascritto.
     * Se esiste una persona con lo stesso nome non ne viene creata un'altra.
     *
     * Se il file non rispetta il formato, i dati non sono caricati e
     * viene sollevata un'eccezione.
     *
     * @param f il file da cui caricare i dati
     *
     * @throws MovidaFileException in caso di errore di caricamento
     */
    public void loadFromFile(File f) throws MovidaFileException {
        try {
            Scanner fileReader = new Scanner(f);
            while (fileReader.hasNextLine()) {
                String title = fileReader.nextLine().split(":")[1].trim();
                //TODO remove prints
                System.out.println(title);
                String year = fileReader.nextLine().split(":")[1].trim();
                System.out.println(year);
                String director = fileReader.nextLine().split(":")[1].trim();
                System.out.println(director);
                String cast = fileReader.nextLine().split(":")[1].trim();
                System.out.println(cast);
                String votes = fileReader.nextLine().split(":")[1].trim();
                System.out.println(votes);
                if (title.isEmpty() || year.isEmpty() || director.isEmpty() || cast.isEmpty() || votes.isEmpty())
                    throw new MovidaFileException();
                //TODO create tokenizer, read method probably also needs data structure where to save data
                // read returns 1 if it finds error
                /*
                if (tokenizer.read(title, year, director, cast, votes))
                    throw new MovidaFileException();
                */
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            throw new MovidaFileException();
        }
    }



    /**
     * Salva tutti i dati su un file.
     *
     * Il file è sovrascritto.
     * Se non è possibile salvare, ad esempio per un problema di permessi o percorsi,
     * viene sollevata un'eccezione.
     *
     * @param f il file su cui salvare i dati
     *
     * @throws MovidaFileException in caso di errore di salvataggio
     */
    public void saveToFile(File f) throws MovidaFileException {

    }


    /**
     * Cancella tutti i dati.
     *
     * Sar� quindi necessario caricarne altri per proseguire.
     */
    public void clear() {
       movies = null;
       people = null;
       collabs = null;
    }

    /**
     * Restituisce il numero di film
     *
     * @return numero di film totali
     */
    public int countMovies() {
        return isInitialized() ? movies.count() : 0;
    }

    /**
     * Restituisce il numero di persone
     *
     * @return numero di persone totali
     */
    public int countPeople() {
        return isInitialized() ? people.count() : 0;
    }

    /**
     * Cancella il film con un dato titolo, se esiste.
     *
     * @param title titolo del film
     * @return <code>true</code> se il film � stato trovato e cancellato,
     * 		   <code>false</code> in caso contrario
     */
    public boolean deleteMovieByTitle(String title) {
        return false;
    }

    /**
     * Restituisce il record associato ad un film
     *
     * @param title il titolo del film
     * @return record associato ad un film
     */
    public Movie getMovieByTitle(String title) {
        return null;
    }

    /**
     * Restituisce il record associato ad una persona, attore o regista
     *
     * @param name il nome della persona
     * @return record associato ad una persona
     */
    public Person getPersonByName(String name) {
        return null;
    }


    /**
     * Restituisce il vettore di tutti i film
     *
     * @return array di film
     */
    public Movie[] getAllMovies() {
        return isInitialized() ? movies.toArray() : new Movie[0];
    }

    /**
     * Restituisce il vettore di tutte le persone
     *
     * @return array di persone
     */
    public Person[] getAllPeople() {
        return isInitialized() ? people.toArray() : new Person[0];
    }


    /** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DELLE COLLAB $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/



    @Override
    public Person[] getDirectCollaboratorsOf(Person actor) {
        return null;
        //return new Person[0];
    }

    @Override
    public Person[] getTeamOf(Person actor) {
        return null;
        //return new Person[0];
    }

    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
        return null;
        //return new Collaboration[0];
    }


    /** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DELLE RICERCHE $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/


    @Override
    public Movie[] searchMoviesByTitle(String title) {
        Movie[] allMovies = getAllMovies();
        LinkedList<Movie> containsTitle = new LinkedList<Movie>();

        for (Movie movie : allMovies) {
            String movieTitle = movie.getTitle();
            if (movieTitle.length() > title.length() && movieTitle.contains(title))
                containsTitle.add(movie);
        }

        return containsTitle.toArray(new Movie[containsTitle.size()]); // (!) forse non serve parametro
    }

    @Override
    public Movie[] searchMoviesInYear(Integer year) {
        Movie[] allMovies = getAllMovies();
        LinkedList<Movie> anno = new LinkedList<Movie>();

        for (Movie movie : allMovies) {
            if (movie.getYear().compareTo(year) == 0)
                anno.add(movie);
        }

        return anno.toArray(new Movie[anno.size()]);
    }

    @Override
    public Movie[] searchMoviesDirectedBy(String name) {
        return null;
        //return new Movie[0];
    }

    @Override
    public Movie[] searchMoviesStarredBy(String name) {
        return null;
        //return new Movie[0];
    }

    @Override
    public Movie[] searchMostVotedMovies(Integer n) {
        Movie[] allMovies = getAllMovies();

        return (Movie[]) ord(allMovies, n, new RatingSorter().reversed(), Movie.class);
    }

    @Override
    public Movie[] searchMostRecentMovies(Integer n) {
        Movie[] allMovies = getAllMovies();

        return (Movie[]) ord(allMovies, n, new YearSorter().reversed(), Movie.class);
    }

    @Override
    public Person[] searchMostActiveActors(Integer N) {
        return null;
        //return new Person[0];
    }

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ M I S C $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    private<V> V[] ord(V[] arr, Integer n, Comparator<V> c, Class<V> cl) { //(!!) testare
       if (n > arr.length || n < 0) // Se n è > del numero di film a disposizione andiamo ad elencarli tutti
           n = arr.length;
       V[] ret;
       if (sort == SortingAlgorithm.SelectionSort) {
           SelectionSort.sort(arr, c);
           ret = Arrays.copyOfRange(arr, 0, n);
       } else {
           MergeSort.sort(arr, n, c.reversed(), cl);
           ret = Arrays.copyOfRange(arr, arr.length - n, arr.length);
       }
       return ret;
    }

    public boolean isInitialized(){
        return movies != null;
    }
}
