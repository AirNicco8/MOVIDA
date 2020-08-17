package src.niccolaibalica.Test;

import java.util.Scanner;

public class HASHtest {

    public static class Movie {

        private String title;
        private Integer year;

        public Movie(String title, Integer year) {
            this.title = title;
            this.year = year;
        }

        public String getTitle(){
          return title;
        }

        public String printtg() {
          return(title + "!" + year.toString());
        }
    }

    public static void main(String[] args) {

        Movie a,b,c,d;
        a = new Movie("Allah", 1998);

        b = new Movie("Hello molly", 1999);

        c = new Movie("Geiger", 2001);

        d = new Movie("Errand", 3444);


        Scanner scan = new Scanner(System.in);
        System.out.println("Hash Table Test\n\n");
        System.out.println("Enter size");
        /* Make object of HashTable */
        hashCon ht = new hashCon(scan.nextInt() );

        ht.insert(a.getTitle(), a);
        ht.insert(b.getTitle(), b);
        ht.insert(c.getTitle(), c);
        ht.insert(d.getTitle(), d);

        char ch;
        /*  Perform HashTable operations  */
        do
        {
            System.out.println("\nHash Table Operations\n");
            System.out.println("2. remove");
            System.out.println("3. get");
            System.out.println("4. clear");
            System.out.println("5. size");

            int choice = scan.nextInt();
            switch (choice)
            {
            case 2 :
                System.out.println("Enter key");
                ht.delete( scan.next() );
                break;
            case 3 :
                System.out.println("Enter key");
                System.out.println("Value = "+ ((Movie)ht.search( scan.next() )).printtg());
                break;
            case 4 :
                ht.makeEmpty();
                System.out.println("Hash Table Cleared\n");
                break;
            case 5 :
                System.out.println("Size = "+ ht.count() );
                break;
            default :
                System.out.println("Wrong Entry \n ");
                break;
            }
            /* Display hash table */
            ht.printHashTable();

            System.out.println("\nDo you want to continue (Type y or n) \n");
            ch = scan.next().charAt(0);
        } while (ch == 'Y'|| ch == 'y');
    }

}
