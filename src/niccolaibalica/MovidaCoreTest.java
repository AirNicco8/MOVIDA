package src.niccolaibalica;

import java.util.Arrays;
import java.io.*;
import src.commons.*;

public class MovidaCoreTest {
	public static void main(String[] args){
		MovidaCore mc = new MovidaCore();
		// user.dir => bin
		String path = "esempio-formato-dati.txt";
		String newpath = "nuovi-dati.txt";
		Movie[] arr = new Movie[10];
		Person[] arrp = new Person[10];

		mc.loadFromFile(mc.toDBfile(path));
		mc.printDicts();
		System.out.println("");
		System.out.println("%%%%%%% test searchMoviesByTitle %%%%%%%%%");

		arr = mc.searchMoviesByTitle("The");
		for(Movie m : arr)
			System.out.println(m.toString());

		System.out.println("");

		arr = mc.searchMoviesByTitle("Pulp");
		for(Movie m : arr)
			System.out.println(m.toString());

		System.out.println("");
		System.out.println("%%%%%%% test searchMoviesInYear %%%%%%%%%");

		arr = mc.searchMoviesInYear(1997);
		for(Movie m : arr){
			String str = String.valueOf(m.getYear());
			System.out.println(m.toString() + "  " + str);
		}

		System.out.println("");

		arr = mc.searchMoviesInYear(1999);
			for(Movie m : arr){
				String str = String.valueOf(m.getYear());
				System.out.println(m.toString() + "  " + str);
			}

		System.out.println("");

		arr = mc.searchMoviesInYear(2345);
			for(Movie m : arr){
				String str = String.valueOf(m.getYear());
				System.out.println(m.toString() + "  " + str);
			}

		System.out.println("");
		System.out.println("%%%%%%% test searchMostVotedMovies %%%%%%%%%");

		arr = mc.searchMostVotedMovies(5);
			for(Movie m : arr){
				String str = String.valueOf(m.getVotes());
				System.out.println(m.toString() + "  " + str);
			}

		System.out.println("");

		arr = mc.searchMostVotedMovies(9);
			for(Movie m : arr){
				String str = String.valueOf(m.getVotes());
				System.out.println(m.toString() + "  " + str);
			}

		System.out.println("");
		System.out.println("%%%%%%% test searchMostRecentMovies %%%%%%%%%");

		arr = mc.searchMostRecentMovies(5);
			for(Movie m : arr){
				String str = String.valueOf(m.getYear());
				System.out.println(m.toString() + "  " + str);
			}

		System.out.println("");

		arr = mc.searchMostRecentMovies(9);
			for(Movie m : arr){
				String str = String.valueOf(m.getYear());
				System.out.println(m.toString() + "  " + str);
			}

		System.out.println("");
		System.out.println("%%%%%%% test searchMoviesDirectedBy %%%%%%%%%");

		arr = mc.searchMoviesDirectedBy("Martin Scorsese");
			for(Movie m : arr){
				String str = m.getDirector().getName();
				System.out.println(m.toString() + "  " + str);
			}

		System.out.println("");

		arr = mc.searchMoviesDirectedBy("Brian De Palma");
			for(Movie m : arr){
				String str = m.getDirector().getName();
				System.out.println(m.toString() + "  " + str);
			}

		System.out.println("");
		System.out.println("%%%%%%% test searchMoviesStarredBy %%%%%%%%%");

		arr = mc.searchMoviesStarredBy("Robert De Niro");
			for(Movie m : arr){
				Person[] ps = m.getCast();
				System.out.println(m.toString());
				for(Person p : ps)
					System.out.println(p.toString());
			}

		System.out.println("");

		arr = mc.searchMoviesStarredBy("Harrison Ford");
			for(Movie m : arr){
				Person[] ps = m.getCast();
				System.out.println(m.toString());
				for(Person p : ps)
					System.out.println(p.toString());
			}

		System.out.println("");
		System.out.println("%%%%%%% test searchMostActiveActors %%%%%%%%%");

		arrp = mc.searchMostActiveActors(5);

			for(Person p : arrp){
				arr = mc.searchMoviesStarredBy(p.getName());
				String str = String.valueOf(arr.length);
				System.out.println(p.toString()+" numero film:"+str);
			}

		System.out.println("");

		arrp = mc.searchMostActiveActors(9);
			for(Person p : arrp){
				arr = mc.searchMoviesStarredBy(p.getName());
				String str = String.valueOf(arr.length);
				System.out.println(p.toString()+" numero film:"+str);
			}


		System.out.println("");
			System.out.println("%%%%%%% test getMovieByTitle   %%%%%%%%%");

			String[] arrs =  mc.getAllMoviesKeys();
		for(String s : arrs){
			System.out.println(s);
		}
		System.out.println("");

		System.out.println("ricerca di Pulp Fiction");
				System.out.println(mc.getMovieByTitle("Pulp Fiction"));

		System.out.println("");

		System.out.println("ricerca di whatliesbeneath");
				System.out.println(mc.getMovieByTitle("whatliesbeneath"));

		System.out.println("");

		System.out.println("ricerca di tttt");
				System.out.println(mc.getMovieByTitle("tttt"));

		System.out.println("");
		System.out.println("%%%%%%% test getPersonByName   %%%%%%%%%");

		arrs =  mc.getAllPeopleKeys();
		for(String s : arrs){
			System.out.println(s);
		}
		System.out.println("");

		System.out.println("ricerca di Cybill Shepherd");
				System.out.println(mc.getPersonByName("Cybill Shepherd"));

		System.out.println("");

		System.out.println("ricerca di jodiefoster");
				System.out.println(mc.getPersonByName("jodiefoster"));

		System.out.println("");

		System.out.println("ricerca di tttt");
				System.out.println(mc.getPersonByName("tttt"));

		System.out.println("");
		System.out.println("%%%%%%% test deleteMovieByTitle   %%%%%%%%%");

		arr =  mc.getAllMovies();
		for(Movie m : arr){
			System.out.println(m);
		}
		System.out.println("");

		System.out.println("cancellazione di the fugitive");
				System.out.println(mc.deleteMovieByTitle("the fugitive"));
		System.out.println("");

		System.out.println("cancellazione di taxi driver");
				System.out.println(mc.deleteMovieByTitle("taxidriver"));
		System.out.println("");

		arr =  mc.getAllMovies();
		for(Movie m : arr){
			System.out.println(m);
		}
		System.out.println("");

		arr = mc.searchMoviesStarredBy("Harrison Ford");
			for(Movie m : arr){
				Person[] ps = m.getCast();
				System.out.println(m.toString());
				for(Person p : ps)
					System.out.println(p.toString());
			}

		System.out.println("");

		arr = mc.searchMoviesStarredBy("Robert De Niro");
			for(Movie m : arr){
				Person[] ps = m.getCast();
				System.out.println(m.toString());
				for(Person p : ps)
					System.out.println(p.toString());
			}

		System.out.println("");
		System.out.println("%%%%%%% test getDirectCollaboratorsOf %%%%%%%%%");

		arrp = mc.getDirectCollaboratorsOf(mc.getPersonByName("Harrison Ford"));

		System.out.println("Collaboratori diretti di Harrison Ford");
			for(Person p : arrp){
				System.out.println(p.toString());
			}

		System.out.println("");

		arrp = mc.getDirectCollaboratorsOf(mc.getPersonByName("Robert De Niro"));

		System.out.println("Collaboratori diretti di Robert De Niro");
			for(Person p : arrp){
				System.out.println(p.toString());
			}

		System.out.println("");
		System.out.println("%%%%%%% test getTeamOf %%%%%%%%%");

		arrp = mc.getTeamOf(mc.getPersonByName("Harrison Ford"));

		System.out.println("team di Harrison Ford (mancano 2 per rimozione the fugitive)");
			for(Person p : arrp){
				System.out.println(p.toString());
			}

		System.out.println("");

		arrp = mc.getTeamOf(mc.getPersonByName("Robert De Niro"));

		System.out.println("team di Robert De Niro");
			for(Person p : arrp){
				System.out.println(p.toString());
			}

		System.out.println("");
		System.out.println("%%%%%%% test maximizeCollaborationsInTheTeamOf %%%%%%%%%");

		Collaboration[] arrc = mc.maximizeCollaborationsInTheTeamOf(mc.getPersonByName("Harrison Ford"));

		System.out.println("");
		System.out.println("%%%%%%% test saveToFile %%%%%%%%%");

		System.out.println(mc.toDBfile(newpath));
		System.out.println("");
		mc.saveToFile(mc.toDBfile(newpath));

		System.out.println("");
		System.out.println("%%%%%%% test setMap %%%%%%%%%");

		mc.setMap(MapImplementation.AVL);

		mc.printDicts();

		System.out.println("");
		System.out.println("%%%%%%% test setSort %%%%%%%%%");

		mc.setSort(SortingAlgorithm.SelectionSort);

		arr = mc.searchMostRecentMovies(5);
			for(Movie m : arr){
				String str = String.valueOf(m.getYear());
				System.out.println(m.toString() + "  " + str);
			}

		System.out.println("");
	}
}
