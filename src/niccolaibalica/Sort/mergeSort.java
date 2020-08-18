package src.niccolaibalica.sort;

public class MergeSort {
    //TODO quale Comparator usare qui? Dobbiamo poter scegliere?
    //TODO c'e' bisogno di tipizzare?
    public void merge(V[] a, V[] l, V[] r, int left, int right, Comparator<V> c) {
        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (c.compare(l[i], r[j])) { //TODO testare come operano i comparators
                a[k++] = r[j++];
            }
            else {
                a[k++] = l[i++];
            }
        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
  }

  public void sort(V[] a, int n, Comparator<V> c) {
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
