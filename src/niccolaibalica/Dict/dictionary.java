package src.niccolaibalica.Dict;

public interface dictionary<V> {

    public void insert(String key,V value);

    public V search(String key);

    public void delete(String key);

    /**
     * Il metodo count fornisce il numero di elementi presenti nella struttura
     * @return Numero di elementi presenti
     */
    public int count();

    /**
     * Il metodo permette di ottenere da un dizionario un Array con i dati inseriti
     * @return Array contenente i dati del dizionario ordinati secondo la loro chiave
     */
    public V[] toArray();
}
