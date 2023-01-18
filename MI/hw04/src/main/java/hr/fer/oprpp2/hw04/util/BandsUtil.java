package hr.fer.oprpp2.hw04.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import hr.fer.oprpp2.hw04.Band;

/**
 * Class that helps reading and writing files connected to voting.
 * 
 * @author Ana Bagić
 *
 */
public class BandsUtil {

	/**
	 * Retrieves the list of bands, read from glasanje-definicija.txt file.
	 * 
	 * @param req servlet request
	 * @return list of bands
	 */
	public static List<Band> getBands(HttpServletRequest req) throws FileNotFoundException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		List<Band> bands = new LinkedList<>();
		try (Scanner sc = new Scanner(new File(fileName))) {
			while(sc.hasNextLine()) {
				String line[] = sc.nextLine().split("\t");
				bands.add(new Band(Integer.parseInt(line[0]), line[1], line[2]));
			}
		}
		
		return bands;
	}
	
	/**
	 * Retrieves mapping of band ids to the number of votes for them, aquired from glasanje-rezultati.txt file.
	 * 
	 * @param req servlet request
	 * @return mapping of band ids to the number of votes for them
	 */
	public static Map<Integer, Integer> getVotes(HttpServletRequest req) throws FileNotFoundException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		Map<Integer, Integer> votes = new HashMap<>();
		try (Scanner sc = new Scanner(new File(fileName))) {
			while(sc.hasNextLine()) {
				String line[] = sc.nextLine().split("\t");
				votes.put(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
			}
		}
		
		return votes;
	}
	
	/**
	 * Writes pairs of band id and number of votes to glasanje-rezultati.txt file.
	 * 
	 * @param req servlet request
	 * @param votes mapping of band ids to the number of votes for them
	 */
	public static void writeVotes(HttpServletRequest req, Map<Integer, Integer> votes) throws FileNotFoundException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		StringBuilder sb = new StringBuilder();
		try (OutputStream os = new FileOutputStream(fileName)) {
			for(var band : votes.entrySet()) {
				String s = band.getKey() + "\t" + band.getValue() + "\n";
				sb.append(s);
			}
			
			os.write(sb.toString().getBytes());
			os.flush();
		}
	}
}
