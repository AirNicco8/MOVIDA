package src.niccolaibalica.sort;

import java.util.Comparator;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import src.commons.Movie;

public class MergeSort{
	public static<V> void sort(V arr[], int l, int r, Comparator<V> c, Class<V> param)
	{
		if (l < r) {
			// Find the middle point
			int m = (l + r) / 2;

			// Sort first and second halves
			sort(arr, l, m, c, param);
			sort(arr, m + 1, r, c, param);

			// Merge the sorted halves
			merge(arr, l, m, r, c, param);
		}
	}

	private static<V> void merge(V arr[], int l, int m, int r, Comparator<V> c, Class<V> param)
	{
		// Find sizes of two subarrays to be merged
		int n1 = m - l + 1;
		int n2 = r - m;

		/* Create temp arrays */
		V[] L = (V[]) Array.newInstance(param, n1);
		V[] R = (V[]) Array.newInstance(param, n2);

		/*Copy data to temp arrays*/
		for (int i = 0; i < n1; ++i)
			L[i] = arr[l + i];
		for (int j = 0; j < n2; ++j)
			R[j] = arr[m + 1 + j];

		/* Merge the temp arrays */

		// Initial indexes of first and second subarrays
		int i = 0, j = 0;

		// Initial index of merged subarry array
		int k = l;
		while (i < n1 && j < n2) {
			if (c.compare(L[i], R[j]) > 0) {
				arr[k] = L[i];
				i++;
			}
			else {
				arr[k] = R[j];
				j++;
			}
			k++;
		}

		/* Copy remaining elements of L[] if any */
		while (i < n1) {
			arr[k] = L[i];
			i++;
			k++;
		}

		/* Copy remaining elements of R[] if any */
		while (j < n2) {
			arr[k] = R[j];
			j++;
			k++;
		}
	}
}
