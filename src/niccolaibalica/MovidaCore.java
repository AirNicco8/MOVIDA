package src.niccolaibalica;

import src.niccolaibalica.asdlab.*;
import src.niccolaibalica.dict.*;
import src.niccolaibalica.sort.*;
import src.commons.*;

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;



public class MovidaCore implements IMovidaSearch,IMovidaConfig,IMovidaDB,IMovidaCollaborations {
    private utils db_utils;
    SortingAlgorithm sort;
    MapImplementation map;
    Dictionary<Movie> movies;
    Dictionary<Person> people;
    GrafoLA collabs; // struttura di Collaborations, nodi = attori | archi = Collaboration(lista di film in comune)
    String pathToDB = System.getProperty("user.dir") + "/../db/";

    public MovidaCore() {
        // TODO debugging / default values
        this.sort = SortingAlgorithm.SelectionSort;
        this.map = MapImplementation.HashConcatenamento;
        this.movies = null;
        this.people = null;
        this.collabs = null;
        this.db_utils = new utils();
    }

/** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DELLA CONFIG $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/


    protected<V> Dictionary<V> createDizionario(Class<V> c) {
        if (map == MapImplementation.AVL)
            return new AvlTree<V>(c);
        else if (map == MapImplementation.HashConcatenamento)
            return new HashCon<V>(120, c);   //TODO replace 42 with valid value
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
                   for (Movie movie : getAllMovies()) {
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

    public void loadFromFile(File f){
        if (!f.exists()) {
            System.out.println("Unvalid path");
            throw new MovidaFileException();
        }

        Movie[] films = db_utils.extFilms(f);

        if (!isInitialized()) {
            movies = createDizionario(Movie.class);
            people = createDizionario(Person.class);
            this.collabs = new GrafoLA();
        }

        for(Movie film: films){
            // Controllo se esiste un film nel DB già caricato con il seguente titolo
            String title = film.getTitle().toLowerCase().trim().replaceAll("\\s", "");
            //System.out.println(title);
                if (this.movies.search(title) != null) {
                    this.movies.delete(title);
                }


            Person director = people.search(film.getDirector().getName()); //cerco se il regista è già nella struttura
                if (director == null)                  //se non lo trovo, lo creo
                {
                    Person dir = new Person(film.getDirector().getName());
                    people.insert(film.getDirector().getName(), dir);
                }

            Person[] cast = film.getCast();
            Person[] actor = new Person[cast.length];
                for (int i = 0; i < actor.length; i++)
                {
                    actor[i] = people.search(cast[i].getName()); //cerco se l'attore è già nella struttura
                    if (actor[i] == null) //se non lo trovo lo creo
                    {
                        Person newActor = new Person(cast[i].getName());
                        people.insert(newActor.getName(), newActor);
                    }
                }


            this.movies.insert(title, film);
            createMovieCollaboration(film);
        }
    }

    public File toDBfile(String path) {
        return new File(pathToDB + path);
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

    public void saveToFile(File f){
        try {

           // Controllo i permessi di scrittura
           if(f.canWrite()){
               // Uso un BufferedWriter passando un FileWriter con append settato a false
               // in modo da sovrascrivere dall'inizio il nuovo file
               BufferedWriter bw = new BufferedWriter(new FileWriter(f.getName(), false));
               Movie[] m = this.movies.toArray();
               for (Movie movie : m) {
                   bw.write("Title: " + movie.getTitle());
                   bw.newLine();
                   bw.write("Year: " + movie.getYear().toString());
                   bw.newLine();
                   bw.write("Director: " + movie.getDirector().getName());
                   bw.newLine();
                   bw.write("Cast: " + movie.getCast());
                   bw.newLine();
                   bw.write("Votes: " + movie.getVotes().toString());
                   bw.newLine();
                   bw.newLine(); // Aggiungo una linea per separare i campi
               }
               bw.close();
           }

       }
       catch(MovidaFileException | IOException e){
           e.getMessage();
           e.printStackTrace();
       }
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
     public Person[] getDirectCollaboratorsOf(Person a) {
         if(isInitialized())
         {
             Nodo node = collabs.nodo(containsActor(a));
             Person[] arr;
             if(node != null){
                 arr = new Person[collabs.gradoUscente(node)];
                 List<Arco> archiIncidenti =  (List <Arco>)collabs.archiUscenti(node);
                 for (int i = 0; i < arr.length; i++) {
                     Nodo dest = archiIncidenti.get(i).dest;
                     arr[i] = (Person)collabs.infoNodo(dest);
                 }
             }
             else
                 arr = new Person[0];
             return arr;
         }
         else {
             return new Person[0];
         }
     }

    @Override
    public Person[] getTeamOf(Person a) {
          if(isInitialized())
         {
             HashMap<Nodo, Boolean> seen = new HashMap<>();   //True nodo visitato, False nodo inesplorato
             Queue<Nodo> front = new LinkedList<>();         //front per BFS
             LinkedList<Person> team = new LinkedList<>();       //Lista da ritornare
             Nodo start = collabs.nodo(containsActor(a));  //Recuperiamo il nodo di origine della visista
             if(start != null){
                 seen.put(start, true);                          //Visitiamo il nodo
                 front.add(start);                              //Aggiungiamo la start alla front
                 while(!front.isEmpty()){
                     Nodo u = front.poll();
                     team.add((Person)collabs.infoNodo(u));   //Aggiungiamo l'attore al team
                     seen.putIfAbsent(u, true);
                     List<Arco> archi = (List<Arco>)collabs.archiUscenti(u);
                     Iterator<Arco> ite =  archi.iterator();
                     while(ite.hasNext()){
                         Arco arco = ite.next();
                         Nodo dest = arco.dest;
                         Boolean visitato = seen.get(dest);       //Controlliamo se il nodo è stato visitato
                         if(visitato == null){
                             front.add(dest);
                             seen.put(dest, true);
                         }
                     }
                 }
             }
             return team.toArray(new Person[team.size()]);
         }
         else{
             return new Person[0];
         }
    }

    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person a) {
        return null;
        //return new Collaboration[0];
    }


    private void createMovieCollaboration(Movie movie){
        Person[] cast = movie.getCast();
        for (int i = 0; i < cast.length - 1; i++) { // Ciclo su tutte le possibili coppie
            for (int j = i + 1; j < cast.length; j++) {
                Person a = cast[i];
                Person b = cast[j];
                //System.out.println(a.getName());
                //System.out.println(b.getName());

                createCollaboration(a, b, movie);
            }
        }
    }

    private void createCollaboration(Person a, Person b, Movie movie){
        Nodo nodoA = null;
        Nodo nodoB = null;
        boolean isNew = false;
        int f,g;

        f = containsActor(a);
        g = containsActor(b);

        //System.out.println(f);
        //System.out.println(g);


        if (f != -1 && g != -1) {                 // Se  i nodi sono entrambi presenti controlliamo se sono adiacenti
            nodoA = collabs.nodo(f);
            nodoB = collabs.nodo(g);

            Arco arco = collabs.sonoAdiacenti(nodoA, nodoB);
            if(arco != null){
                Collaboration collab = (Collaboration) collabs.infoArco(arco);   //Se sono adiacenti esiste già una collaborazione
                if(!collab.searchMovie(movie))
                    collab.addMovie(movie);                       //Aggiungiamo il movie alla lista dei film
            }
            else
                isNew = true;
        }
        else{
                if (f == -1) {                            //Se anche uno di loro non è presente si tratta allora di una nuova collaborazione
                    nodoA = collabs.aggiungiNodo(a);
                }
                else{
                    nodoA = collabs.nodo(f);
                }

                if(g == -1){
                    nodoB = collabs.aggiungiNodo(b);
                }
                else{
                  nodoB = collabs.nodo(g);
                }
                    isNew = true;
        }

        if(isNew){
            Collaboration newColl = new Collaboration(a, b);
            newColl.addMovie(movie);
            Person t = (Person) nodoA.info;
            Person y = (Person) nodoB.info;

            //System.out.println(t.getName());
            //System.out.println(y.getName());

            collabs.aggiungiArco(nodoA, nodoB, newColl);
            collabs.aggiungiArco(nodoB, nodoA, newColl);
        }

    }

    private int containsActor(Person a){ //ritorna indice noda nel grafo se presente, -1 se assente

        NodoLA[] arr = collabs.nodiLA();

        for(NodoLA n : arr){
          Person tmp = (Person) collabs.infoNodo(n);
          if(tmp.getName().compareToIgnoreCase(a.getName()) == 0)
            return n.indice;
        }

        return -1;
    }

    /** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DELLE RICERCHE $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/


    @Override
    public Movie[] searchMoviesByTitle(String title) { //TESTED e funziona ma fa differenza sulle maiuscole
        Movie[] allMovies = getAllMovies();            // fixare o lasciare così?
        LinkedList<Movie> containsTitle = new LinkedList<Movie>();

        for (Movie movie : allMovies) {
            String movieTitle = movie.getTitle();
            //System.out.println(movieTitle);
            if (movieTitle.length() >= title.length() && movieTitle.contains(title))
                containsTitle.add(movie);
        }

        return containsTitle.toArray(new Movie[containsTitle.size()]); // (!) forse non serve parametro
    }

    @Override
    public Movie[] searchMoviesInYear(Integer year) { // WORKING
        Movie[] allMovies = getAllMovies();
        LinkedList<Movie> anno = new LinkedList<Movie>();

        for (Movie movie : allMovies) {
            if (movie.getYear().compareTo(year) == 0)
                anno.add(movie);
        }

        return anno.toArray(new Movie[anno.size()]);
    }

    @Override
    public Movie[] searchMoviesDirectedBy(String name) {// WORKING
        Movie[] allMovies = getAllMovies();
        LinkedList<Movie> ret = new LinkedList<Movie>();

        for (Movie m : allMovies) {
              if (m.getDirector().getName().compareToIgnoreCase(name.trim().toLowerCase()) == 0) {
                ret.add(m);
              }
        }
        return ret.toArray(new Movie[ret.size()]);
    }

    @Override
    public Movie[] searchMoviesStarredBy(String name) {// WORKING
        Movie[] allMovies = getAllMovies();
        LinkedList<Movie> ret = new LinkedList<Movie>();

        for (Movie m : allMovies) {
            for (Person p : m.getCast()) {
                if (p.getName().compareToIgnoreCase(name.trim().toLowerCase()) == 0) {
                  ret.add(m);
                  break;
                }
            }
        }
        return ret.toArray(new Movie[ret.size()]);
    }

    @Override
    public Movie[] searchMostVotedMovies(Integer n) { // WORKING
        Movie[] allMovies = getAllMovies();

        return (Movie[]) ord(allMovies, n, new RatingSorter().reversed(), Movie.class);
    }

    @Override
    public Movie[] searchMostRecentMovies(Integer n) { // WORKING
        Movie[] allMovies = getAllMovies();

        return (Movie[]) ord(allMovies, n, new YearSorter().reversed(), Movie.class);
    }

    @Override
    public Person[] searchMostActiveActors(Integer n) { // WORKING
        Person[] allPeople = getAllPeople();
        Person[] ret = new Person[n];
        int tot, i=0;
        Map<Person, Integer> myMap = new HashMap<Person, Integer>();

        for(Person p : allPeople){
          tot = searchMoviesStarredBy(p.getName()).length;
          myMap.put(p, tot);
        }

        Set<Entry<Person, Integer>> entries = myMap.entrySet();

        Comparator<Entry<Person, Integer>> valueComparator = new Comparator<Entry<Person, Integer>>() { @Override public int compare(Entry<Person, Integer> e1, Entry<Person, Integer> e2) { Integer v1 = e1.getValue(); Integer v2 = e2.getValue(); return v1.compareTo(v2); } };

        // Sort method needs a List, so let's first convert Set to List in Java
        List<Entry<Person, Integer>> listOfEntries = new ArrayList<Entry<Person, Integer>>(entries);

        // sorting HashMap by values using comparator
        Collections.sort(listOfEntries, valueComparator.reversed() );

        LinkedHashMap<Person, Integer> sortedByValue = new LinkedHashMap<Person, Integer>(listOfEntries.size());

        for(Entry<Person, Integer> entry : listOfEntries){
            sortedByValue.put(entry.getKey(), entry.getValue());
        }

        //System.out.println("HashMap after sorting entries by values ");
        Set<Entry<Person, Integer>> entrySetSortedByValue = sortedByValue.entrySet();

        for(Entry<Person, Integer> mapping : entrySetSortedByValue){
            //System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
            ret[i] = mapping.getKey();
            i++;
            if(i == n)
              break;
        }

        return ret;
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

    public void printDicts(){
      Movie[] ms = new Movie[movies.count()];
      ms = movies.toArray();

      for(int i=0; i < movies.count(); i++){
          System.out.println(ms[i].getTitle());
      }
      System.out.println("//////");
      System.out.println(Arrays.toString(people.toArray()));
    }
}
