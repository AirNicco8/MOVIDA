package src.niccolaibalica;

import src.niccolaibalica.dict.Dictionary;
import src.commons.*;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Scanner;


public class MovidaCore implements IMovidaSearch,IMovidaConfig,IMovidaDB,IMovidaCollaborations {
    private utils db_utils;
    SortingAlgorithm sort;
    MapImplementation map;

    public MovidaCore() {
        // TODO debugging / default values
        this.sort = SortingAlgorithm.SelectionSort;
        this.map = MapImplementation.AVL;
    }

    /** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DELLA CONFIG $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/


    protected Dictionary<V> createDizionario(V c) {
        if (map == MapImplementation.AVL)
            return new AvlTree<V>(c);
        else if (map == MapImplementation.HashConcatenamento)
            return new HashCon<V>(c);
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
        return false;
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
    public void loadFromFile(File f) {
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

    }




    /**
     * Cancella tutti i dati.
     *
     * Sar� quindi necessario caricarne altri per proseguire.
     */
    public void clear(){
    }

    /**
     * Restituisce il numero di film
     *
     * @return numero di film totali
     */
    public int countMovies(){
    }

    /**
     * Restituisce il numero di persone
     *
     * @return numero di persone totali
     */
    public int countPeople(){

    }

    /**
     * Cancella il film con un dato titolo, se esiste.
     *
     * @param title titolo del film
     * @return <code>true</code> se il film � stato trovato e cancellato,
     * 		   <code>false</code> in caso contrario
     */
    public boolean deleteMovieByTitle(String title){

    }

    /**
     * Restituisce il record associato ad un film
     *
     * @param title il titolo del film
     * @return record associato ad un film
     */
    public Movie getMovieByTitle(String title){

    }

    /**
     * Restituisce il record associato ad una persona, attore o regista
     *
     * @param name il nome della persona
     * @return record associato ad una persona
     */
    public Person getPersonByName(String name){

    }


    /**
     * Restituisce il vettore di tutti i film
     *
     * @return array di film
     */
    public Movie[] getAllMovies(){

    }

    /**
     * Restituisce il vettore di tutte le persone
     *
     * @return array di persone
     */
    public Person[] getAllPeople();


    /** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DELLE COLLAB $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/



    @Override
    public Person[] getDirectCollaboratorsOf(Person actor) {
        return new Person[0];
    }

    @Override
    public Person[] getTeamOf(Person actor) {
        return new Person[0];
    }

    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
        return new Collaboration[0];
    }


    /** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ GESTIONE DELLE RICERCHE $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**/


    @Override
    public Movie[] searchMoviesByTitle(String title) {
        return new Movie[0];
    }

    @Override
    public Movie[] searchMoviesInYear(Integer year) {
        return new Movie[0];
    }

    @Override
    public Movie[] searchMoviesDirectedBy(String name) {
        return new Movie[0];
    }

    @Override
    public Movie[] searchMoviesStarredBy(String name) {
        return new Movie[0];
    }

    @Override
    public Movie[] searchMostVotedMovies(Integer N) {
        return new Movie[0];
    }

    @Override
    public Movie[] searchMostRecentMovies(Integer N) {
        return new Movie[0];
    }

    @Override
    public Person[] searchMostActiveActors(Integer N) {
        return new Person[0];
    }
}
