package Kauppamatkustaja;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Teht2 {
	/**
	 * Valitettavasti lyhyet selostuksen eivät ole vahvuuksiani, joten ne on
	 * kirjoitettu pitkästi :)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader reader;
		// alustukset
		String path = "Matkat.txt";
		ArrayList<String> city = new ArrayList<String>();
		int[][] matrix = null;
		String[] result = new String[7];
		/**
		 * Luetaan tiedoston "header" omaan listaan ja kaupunkien etäisyydet matriisiin
		 * riveittäin/sarakkeittain
		 */
		try {
			reader = new BufferedReader(new FileReader(path));
			String line;
			int ctr = 0;

			while ((line = reader.readLine()) != null) {
				result = line.split("\\t");
				if (matrix == null) {
					matrix = new int[result.length][result.length];
				}
				try {
					for (int col = 0; col < result.length; col++) {
						matrix[ctr][col] = Integer.parseInt(result[col]);
					}
					ctr += 1;
				} catch (Exception e) {
					for (String string : result) {
						city.add(string);
					}
				}
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		Traveller t = new Traveller(matrix, city);
		t.greedyBoy();
		
	}
}

/**
 * Luokassa ratkaistaan lyhin mahdollinen reitti halutusta lähtöpaikasta
 * käyttämällä ahnetta algoritmia ja tulostetaan lopuksi reitti (joka on
 * osittain kyllä kopioitu suoraan netistä, mutta
 * kommenoitu kuitenkin auki, eli ymmärrän joka kohdan mitä tapahtuu)
 */
class Traveller {
	private ArrayList<String> cList;
	private int[][] tsp;

	public Traveller(int[][] matrix, ArrayList<String> city) {
		this.cList = new ArrayList<String>(city);
		this.tsp = matrix;
	}

	public void greedyBoy() {
		/**
		 * Vaihtamalla startPoint arvoksi kyseisen kaupungin numero saadaan siitä tehtyä
		 * lähtöpaikka 
		 * tampere = 0 
		 * turku = 1 
		 * pori = 2 
		 * kuopio = 3 
		 * hämeenlinna = 4 
		 * lahti =5 
		 * helsinki = 6
		 */

		int startPoint = 3;

		/**
		 * 
		 * alustetaan muuttujat. i&j muuttujat luovat matrsiisin jossa kaupunkien
		 * "sijainnit" ovat i = matriisin sarakkeet j = matriisin rivit (tälle annetaan
		 * arvoksi lähtöpaikan sarakkeen haluttu sijainti, eli missä arvo on 0"km")
		 */

		int sum = 0;
		int counter = 0;
		int j = 0, i = startPoint;
		int min = Integer.MAX_VALUE;
		List<Integer> visitedRouteList = new ArrayList<>();

		/**
		 * Lisätään aloituspaikka visitedroutelistiin jotta sinne ei voi jatkossa enää
		 * mennä ja alustetaan route array kaupunkien määrällä
		 */
		visitedRouteList.add(startPoint);
		int[] route = new int[tsp.length];

		while (true) {

			// lopetetaan kun kaikki on käyty läpi
			if (counter >= tsp[i].length - 1) {
				break;
			}

			/**
			 * tässä ehtolauseessa etsitään kyseisen kierroksen(eli missä kaupungissa olet
			 * nyt) pienin arvo ja tallennetaan se min-muuttujaan, eli min-muuttujassa
			 * säilytetään pienintä arvoa kunnes kaikkien kaupunkien etäisyydet on
			 * tarkistettu. Ehtolauseet on pilkottu pienemmiksi lukemisen helpottamiseksi
			 */
			if (j != i && !(visitedRouteList.contains(j))) {
				if (tsp[i][j] < min) {
					min = tsp[i][j];
					route[counter] = j + 1;
				}
			}

			j++;
			/**
			 * kun kaikki kaupungit on kierretty lisätään summaan kyseisen kierroksen
			 * pienimmän matkan pituus samalla lisätään kaupunki listaan jottei sinne mennä
			 * uudestaan seuraavilla kierroksilla nollataan j-laskuri seuraavaa kierrosta
			 * varten ja siirrytään matriisin riville kaupunki==0
			 */
			if (j == tsp[i].length) {
				sum += min;
				min = Integer.MAX_VALUE;
				visitedRouteList.add(route[counter] - 1);
				j = 0;
				i = route[counter] - 1;
				counter++;
			}
		}

		/**
		 * laitetaan routen viimeiseksi arvoksi jäljelle jäänyt matka toiseksi
		 * viimeisestä kaupungista viimeiseen eli takaisin lähtöpaikkaan
		 */
		i = route[counter - 1] - 1;
		for (j = 0; j < tsp.length; j++) {
			if ((i != j) && tsp[i][j] < min && j == startPoint) {
				min = tsp[i][j];
				route[counter] = j + 1;
			}
		}
		sum += min;

		/**
		 * Tulostetaan saatu lyhyin reitti ja sen yhteenlaskettu pituus
		 */
		System.out.println("Lyhyimmän reitin pituus käyttämällä ahnetta algorithmiä: " + sum + " km");
		System.out.println("Lähtö:" + cList.get(startPoint) + "");
		for (int k = 0; k < route.length; k++) {
			if(k<=route.length-2){
				System.out.print(cList.get(route[k]-1) + "->");
			}			
		}
		System.out.println();
		System.out.println("Ja takaisin lähtökaupunkiin " +cList.get(startPoint));
	}
}

