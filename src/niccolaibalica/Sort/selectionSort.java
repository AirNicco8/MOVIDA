package src.niccolaibalica.sort;

import src.niccolaibalica.sort.Comparators;

public class SelectionSort<V> {

  public static<V> void sort(V[] arr, Comparators<V> c){
        for (int i = 0; i < arr.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < arr.length; j++){
                if (c.compare(arr[j], arr[index]) < 0){ // (!!) testare come operano i comparators
                    index = j; //searching for lowest index
                }
            }
            /*int smallerNumber = arr[index];
            arr[index] = arr[i];
            arr[i] = smallerNumber;  */
            swap(arr, index, i);
        }
    }

}
