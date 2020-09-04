package src.niccolaibalica.sort;

import java.util.Comparator;
import src.niccolaibalica.utils;

public class SelectionSort{

	public static<V> void sort(V[] arr, Comparator<V> c){
		utils u = new utils();

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
			u.swap(arr, index, i);
		}
	}

}
