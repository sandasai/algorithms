package kargerMinCut;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class minCutMain {
	
	/**
	 * Solves the minimum cut between any two sets of vertices in a graph; vertices are represented as Integers
	 * @param args 0 - the file which represents a graph where each row is an adjacency list
	 * 			   1 - # of iterations to run the cut
	 */
	public static void main(String[] args) {
		List<String> tempList = new ArrayList<String>();
		String fileName = args[0];
		importFileLinesToArray(fileName, tempList);
		MinCut problem = new MinCut();
		int totalTrials = Integer.parseInt(args[1]);
		
		problem.buildGraph(tempList);
		int min = problem.solve();
		
		//Iteratively run the cut due to low probability of success of algorithm; record the min
		for (int i = 0; i < totalTrials; i++) {
			if (i % 50 == 0) {
				System.out.println("... iteration: " + i );
			}
			problem.buildGraph(tempList);
			int solution = problem.solve();
			if (solution < min) {
				min = solution; 
			}
		}
		System.out.println("minimum cut: " + min);
	}
	
	private static void importFileLinesToArray(String fileName, List<String> arr) {
        String line = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                String text = line;
                text = text.replace("\n", "");
                arr.add(line);
            }   
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }	
	}
}
