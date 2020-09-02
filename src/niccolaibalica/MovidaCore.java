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
import java.util.PriorityQueue;
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
        this.sort = SortingAlgorithm.MergeSort;
        this.map = MapImplementation.HashConcatenamento;
        this.movies = null;
        this.people = null;
        this.collabs = null;
        this.db_utils = new utils();
    }

/** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DELLA CONFIG $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/


    protected<V> Dictionary<V> createDizionario(Class<V> c, int n) {
        if (map == MapImplementation.AVL)
            return new AvlTree<V>(c);
        else if (map == MapImplementation.HashConcatenamento)
            return new HashCon<V>(n, c); 
        return null;
    }

    @Override
    public boolean setSort(SortingAlgorithm a) { // WORKING
        if (a != sort) {
            if (a == SortingAlgorithm.SelectionSort || a == SortingAlgorithm.MergeSort) {
                sort = a;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setMap(MapImplementation m) { // WORKING
       boolean rit = false;
       if (m != map) {
           if (m == MapImplementation.AVL || m == MapImplementation.HashConcatenamento) {
               map = m;
               if( movies != null && people != null){
                   Dictionary<Person> newPeople = createDizionario(Person.class, movies.count());
                   Dictionary<Movie> newMovies = createDizionario(Movie.class, people.count());
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
        int lines = 0;

        try {
              BufferedReader reader = new BufferedReader(new FileReader(f));
              while (reader.readLine() != null) lines++;
              reader.close();
            }
        catch(MovidaFileException | IOException e){
                e.getMessage();
                e.printStackTrace();
            }

        if (!dbPop()) {
            int filmNum = (lines-(lines/10))/5; //stima film da numero righe file
            int peopleNum = filmNum * 10;
            System.out.println(filmNum);
            System.out.println(peopleNum);
            System.out.println("");
            movies = createDizionario(Movie.class, filmNum);
            people = createDizionario(Person.class, peopleNum);
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
           if(f.canWrite()){
                System.out.println("in save to file");
               // Uso un BufferedWriter passando un FileWriter con append settato a false
               // in modo da sovrascrivere dall'inizio il nuovo file
               BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
               Movie[] m = this.movies.toArray();
               for (Movie movie : m) {
                  System.out.println(movie.toString());
                   bw.write("Title: " + movie.getTitle());
                   bw.newLine();
                   bw.write("Year: " + movie.getYear().toString());
                   bw.newLine();
                   bw.write("Director: " + movie.getDirector().getName());
                   bw.newLine();
                   Person[] cast = movie.getCast();
                   String wrCast = "";
                    for(int i=0; i<cast.length; i++)
                    {
                        wrCast += cast[i].getName();
                        if( (i + 1) != cast.length ){
                            wrCast += ", ";
                        }
                        else{                         //ultimo attore
                            wrCast += '\n';
                        }
                    }
                   bw.write("Cast: " + wrCast);
                   bw.write("Votes: " + movie.getVotes().toString());
                   bw.newLine();
                   bw.newLine();
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
        return dbPop() ? movies.count() : 0;
    }

    /**
     * Restituisce il numero di persone
     *
     * @return numero di persone totali
     */
    public int countPeople() {
        return dbPop() ? people.count() : 0;
    }

    /**
     * Cancella il film con un dato titolo, se esiste.
     *
     * @param title titolo del film
     * @return <code>true</code> se il film � stato trovato e cancellato,
     * 		   <code>false</code> in caso contrario
     */
    public boolean deleteMovieByTitle(String title) {// WORKING
        if(dbPop())
        {
            int tot = movies.count()-1;
            Movie dMovie = movies.search(title);
            if (dMovie != null) {
                deleteCollaborationsOfMovie(dMovie);
                movies.delete(title);
            }
            return (movies.count() == tot);
        }
        else{
            return false;
        }
    }

    /**
     * Restituisce il record associato ad un film
     *
     * @param title il titolo del film
     * @return record associato ad un film
     */
    public Movie getMovieByTitle(String title) { // WORKING, - movie keys are title with no spaces and lowercase
        return dbPop() ? movies.search(title) : null;
    }

    /**
     * Restituisce il record associato ad una persona, attore o regista
     *
     * @param name il nome della persona
     * @return record associato ad una persona
     */
    public Person getPersonByName(String name) {// WORKING, - person keys are names with spaces and uppercase
        return dbPop() ? people.search(name) : null;
    }


    /**
     * Restituisce il vettore di tutti i film
     *
     * @return array di film
     */
    public Movie[] getAllMovies() {
        return dbPop() ? movies.toArray() : new Movie[0];
    }

    public String[] getAllMoviesKeys() {
        return dbPop() ? movies.toArrayKeys() : new String[0];
    }

    /**
     * Restituisce il vettore di tutte le persone
     *
     * @return array di persone
     */
    public Person[] getAllPeople() {
        return dbPop() ? people.toArray() : new Person[0];
    }

    public String[] getAllPeopleKeys() {
        return dbPop() ? people.toArrayKeys() : new String[0];
    }


/** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DELLE COLLAB $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/

    @Override
     public Person[] getDirectCollaboratorsOf(Person a) { // WORKING
         if(dbPop())
         {
             Nodo node = collabs.nodo(containsActor(a));
             Person[] arr;
             if(node != null){
                 arr = new Person[collabs.gradoUscente(node)];
                 List<Arco> arcs =  (List <Arco>)collabs.archiUscenti(node);
                 for (int i = 0; i < arr.length; i++) {
                     Nodo dest = arcs.get(i).dest;
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
    public Person[] getTeamOf(Person a) {// WORKING
          if(dbPop())
         {
             HashMap<Nodo, Boolean> seen = new HashMap<>();   // seen nodes
             Queue<Nodo> front = new LinkedList<>();         // frontiera BFS
             LinkedList<Person> team = new LinkedList<>();
             Nodo start = collabs.nodo(containsActor(a));
             if(start != null){
                 seen.put(start, true);
                 front.add(start);
                 while(!front.isEmpty()){
                     Nodo u = front.poll();                 // estrazione nodo dalla frontiera
                     team.add((Person)collabs.infoNodo(u));
                     seen.putIfAbsent(u, true);
                     List<Arco> arcs = (List<Arco>)collabs.archiUscenti(u);
                     for(Arco arc : arcs){
                         Nodo dest = arc.dest;
                         Boolean visitato = seen.get(dest);
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

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ M E T O D I   M S T $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person a) { // WORKING ?
      if(dbPop())
          return findMST(getTeamOf(a));
      else
          return new Collaboration[0];
    }

    public Collaboration[] findMST(Person[] arrp){

        GrafoLA subgraph = new GrafoLA();
        int gg=1;

        for(Person p : arrp){
             Nodo n = collabs.nodo(containsActor(p));
             subgraph.aggiungiNodo(n.info);
        }

        for(Arco ar : collabs.archi()){
            if((containsActorSub((Person)ar.orig.info, subgraph)!= -1 ) && (containsActorSub((Person)ar.dest.info, subgraph)!= -1)){
              //System.out.println("in if");
              //System.out.println("Nodo origine: "+ar.orig.info);
              //System.out.println("Nodo destination: "+ar.dest.info);

              subgraph.aggiungiArco(subgraph.nodo(containsActorSub((Person)ar.orig.info, subgraph)), subgraph.nodo(containsActorSub((Person)ar.dest.info, subgraph)), ar.info);
            }
            gg++;
        }
        /*System.out.println("nodi sub");
        for(Nodo n : subgraph.nodi()){
             System.out.println(n.info);
        }
        System.out.println("");
        System.out.println("archi sub");
        System.out.println(subgraph.numArchi());
        for(Arco ar : subgraph.archi()){
            System.out.println(ar.info);
        }
        System.out.println("");*/

        PriorityQueue<Arco> pq = new PriorityQueue<>(subgraph.numArchi(), new EdgeSorter());

        Arco[] arr = subgraph.archi();

        //add all the edges to priority queue, //sort the edges on weights
        for (int i = 0; i < subgraph.numArchi(); i++) {
            pq.add(arr[i]);
        }

        //create a parent []
        int [] parent = new int[collabs.numNodi()];

        //makeset
        makeSet(parent);

        ArrayList<Collaboration> mst = new ArrayList<>();
        ArrayList<Arco> mstp = new ArrayList<>();

        //process vertices - 1 edges
        int index = 0;
        while(index < subgraph.numNodi()-1){
            Arco edge = pq.remove();
            //check if adding this edge creates a cycle
            int x_set = find(parent, subgraph.indice(edge.orig));
            int y_set = find(parent, subgraph.indice(edge.dest));

            if(x_set==y_set){
                //ignore, will create cycle
            }else {
                //add it to our final result
                mst.add((Collaboration)edge.info);
                mstp.add(edge);
                index++;
                union(parent,x_set,y_set);
            }
        }
        //print MST
        System.out.println("Minimum Spanning Tree: ");

        printGraph(mstp);

        return mst.toArray(new Collaboration[mst.size()]);
    }

    public void makeSet(int [] parent){
            //Make set- creating a new element with a parent pointer to itself.
            for (int i = 0; i < parent.length; i++) {
                parent[i] = i;
            }
        }

    public int find(int [] parent, int vertex){
        //chain of parent pointers from x upwards through the tree
        // until an element is reached whose parent is itself
        if(parent[vertex]!=vertex)
            return find(parent, parent[vertex]);;
        return vertex;
    }

    public void union(int [] parent, int x, int y){
        int x_set_parent = find(parent, x);
        int y_set_parent = find(parent, y);
        //make x as parent of y
        parent[y_set_parent] = x_set_parent;
    }

    public void printGraph(ArrayList<Arco> edgeList){
        for (int i = 0; i <edgeList.size() ; i++) {
            Arco edge = edgeList.get(i);
            System.out.println("Edge-" + i + " source: " + edge.orig.info +
                    " destination: " + edge.dest.info +
                    " weight: " + ((Collaboration)edge.info).getScore());
        }
    }

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ C O N T I N U A    C O L L A B S $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$


    private void createMovieCollaboration(Movie movie){
        Person[] cast = movie.getCast();
        for (int i = 0; i < cast.length - 1; i++) { // Ciclo su tutte le possibili coppie
            for (int j = i + 1; j < cast.length; j++) {
                Person a = cast[i];
                Person b = cast[j];

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

    private int containsActorSub(Person a, GrafoLA g){ //ritorna indice noda nel grafo se presente, -1 se assente

        NodoLA[] arr = g.nodiLA();

        for(NodoLA n : arr){
          Person tmp = (Person) g.infoNodo(n);
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

        for (Movie sMovie : allMovies) {
              if (sMovie.getDirector().getName().compareToIgnoreCase(name.trim().toLowerCase()) == 0) {
                ret.add(sMovie);
              }
        }
        return ret.toArray(new Movie[ret.size()]);
    }

    @Override
    public Movie[] searchMoviesStarredBy(String name) {// WORKING
        Movie[] allMovies = getAllMovies();
        LinkedList<Movie> ret = new LinkedList<Movie>();

        for (Movie sMovie : allMovies) {
            for (Person p : sMovie.getCast()) {
                if (p.getName().compareToIgnoreCase(name.trim().toLowerCase()) == 0) {
                  ret.add(sMovie);
                  break;
                }
            }
        }
        return ret.toArray(new Movie[ret.size()]);
    }

    @Override
    public Movie[] searchMostVotedMovies(Integer n) { // WORKING
        Movie[] allMovies = getAllMovies();

        if(sort == SortingAlgorithm.SelectionSort)
            return (Movie[]) ord(allMovies, n, new RatingSorter().reversed(), Movie.class);
        else
            return (Movie[]) ord(allMovies, n, new RatingSorter(), Movie.class);
    }

    @Override
    public Movie[] searchMostRecentMovies(Integer n) { // WORKING
        Movie[] allMovies = getAllMovies();

        if(sort == SortingAlgorithm.SelectionSort)
            return (Movie[]) ord(allMovies, n, new YearSorter().reversed(), Movie.class);
        else
            return (Movie[]) ord(allMovies, n, new YearSorter(), Movie.class);
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
           MergeSort.sort(arr, 0, movies.count()-1, c, cl); // reversed?
           ret = Arrays.copyOfRange(arr, 0, n);
       }
       return ret;
    }

    private void deleteCollaborationsOfMovie(Movie movie){
        Person[] cast = movie.getCast();
        for (int i = 0; i < cast.length-1; i++) {
            for (int j = i+1; j < cast.length; j++) {
                Person a = cast[i];
                Person b = cast[j];
                deleteCollaboration(a, b, movie);
            }
        }
    }

    private void deleteCollaboration(Person a, Person b, Movie movie){
        Nodo nodoA = collabs.nodo(containsActor(a)); // Cerchiamo i nostri nodi nella hash map
        Nodo nodoB = collabs.nodo(containsActor(b));

        Arco arcoAB = collabs.sonoAdiacenti(nodoA, nodoB); // Ricerchiamo i nostri archi
        Arco arcoBA = collabs.sonoAdiacenti(nodoB, nodoA);

        Collaboration collab = (Collaboration) collabs.infoArco(arcoAB);

        collab.deleteMovie(movie); // Rimuoviamo il film dalla collaborazione

        if (collab.isEmpty()) { // Se non vi è alcun film in cui gli attori hanno recitato insieme
            collabs.rimuoviArco(arcoAB); // Rimuoviamo gli archi e le collaborazioni
            collabs.rimuoviArco(arcoBA);
            if (collabs.gradoUscente(nodoA) == 0){ // Se il nostro nodo non ha più archi andiamo ad eliminarlo
                collabs.rimuoviNodo(nodoA);
            }
            if (collabs.gradoUscente(nodoB) == 0){
                collabs.rimuoviNodo(nodoB);
            }
        }
    }

    public boolean dbPop(){
        return movies != null;
    }

    public void printDicts(){
      Movie[] ms = new Movie[movies.count()];
      System.out.println(movies.count());
      ms = movies.toArray();

      for(int i=0; i < ms.length; i++){
          System.out.println(ms[i]);
      }
      System.out.println("//////");
      System.out.println(Arrays.toString(people.toArray()));
    }
}
