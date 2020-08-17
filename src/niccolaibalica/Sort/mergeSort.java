package src.niccolaibalica.Sort;

public class MergeSort {

  public static<V> void merge(V[] a, V[] l, V[] r, int left, int right, Comparator<V> c) {
      int i = 0, j = 0, k = 0;
      while (i < left && j < right) {
          if (c.compare(l[i], r[j]) <= 0) { // (!!) testare come operano i comparators
              a[k++] = l[i++];
          }
          else {
              a[k++] = r[j++];
          }
      }
      while (i < left) {
          a[k++] = l[i++];
      }
      while (j < right) {
          a[k++] = r[j++];
      }
  }

  public static<V> void sort(V[] a, int n, Comparator<V> c) {
      if (n < 2) {
          return;
      }
      int mid = n / 2;
      V[] l = new V[mid];
      V[] r = new V[n - mid];

      for (int i = 0; i < mid; i++) {
          l[i] = a[i];
      }
      for (int i = mid; i < n; i++) {
          r[i - mid] = a[i];
      }
      sort(l, mid, c);
      sort(r, n - mid, c);

      merge(a, l, r, mid, n - mid, c);
      }
  }
