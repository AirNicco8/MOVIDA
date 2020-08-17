package src.niccolaibalica.Dict;

public class hashCon<V> implements dictionary<V> {

  class conHashEntry<V>{
      conHashEntry next;
      String key;
      V data;

      /* Constructor */
      conHashEntry(String key, V data)
      {
          this.key = key;
          this.data = data;
          this.next = null;
      }

  }

  private int TABLE_SIZE;
  private int size;
  private conHashEntry[] table;

  /* Constructor */
  public hashCon(int ts) {
        size = 0;
        TABLE_SIZE = ts;
        table = new conHashEntry[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++)
            table[i] = null;
  }

  //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ U T I L S $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    /* Function to get number of key-value pairs */
  public int getSize(){
        return size;
  }
    /* Function to clear hash table */
  public void makeEmpty(){
        for (int i = 0; i < TABLE_SIZE; i++)
            table[i] = null;
  }

  private int myHash(String x){
       int hashVal = x.hashCode( );
       hashVal %= TABLE_SIZE;
       if (hashVal < 0)
           hashVal += TABLE_SIZE;
       return hashVal;
   }

   /* Function to print hash table */
   public void printHashTable(){
       for (int i = 0; i < TABLE_SIZE; i++)
       {
           System.out.print("\nBucket "+ (i + 1) +" : ");
           conHashEntry entry = table[i];
           while (entry != null)
           {
               System.out.print(entry.key +" ");
               entry = entry.next;
           }
       }
   }

   public int count(){
     return getSize();
   }

   // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ R I C E R C A $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

   public V search(String k) throws ExceptionKeyNotFound{
     int hash = (myHash(k) % TABLE_SIZE);
        if (table[hash] == null)
            throw new ExceptionKeyNotFound();
        else
        {
            conHashEntry entry = table[hash];
            while (entry != null && !entry.key.equals(k))
                entry = entry.next;
            if (entry == null)
                throw new ExceptionKeyNotFound();
            else
                return ((conHashEntry<V>)entry).data;
        }
   }

   // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ D E L E T E $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

   public void delete(String k){
     int hash = (myHash(k) % TABLE_SIZE);
        if (table[hash] != null)
        {
            conHashEntry prevEntry = null;
            conHashEntry entry = table[hash];
            while (entry.next != null && !entry.key.equals(k))
            {
                prevEntry = entry;
                entry = entry.next;
            }
            if (entry.key.equals(k))
            {
                if (prevEntry == null)
                    table[hash] = entry.next;
                else
                    prevEntry.next = entry.next;
                size--;
            }
        }
   }

   // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ I N S E R I M E N T O $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

   public void insert(String k, V val){
     int hash = (myHash(k) % TABLE_SIZE);
        if (table[hash] == null)
            table[hash] = new conHashEntry(k, val);
        else
        {
             conHashEntry entry = table[hash];
            while (entry.next != null && !entry.key.equals(k))
                entry = entry.next;
            if (entry.key.equals(k))
                entry.data = val;
            else
                entry.next = new conHashEntry(k, val);
        }
        size++;
   }
}
