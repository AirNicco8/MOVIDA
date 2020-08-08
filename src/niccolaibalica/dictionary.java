package src.niccolaibalica;

import java.util.*;

    public interface dictionary<K, V>
    {

        void insert(K key,V value);

        V search(K key);

        void delete(K key);

    }