import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Reads in twitter information and makes maps by journal
 *
 */
public class TwitterReader {

    private Scanner scan;

    /**
     * Updates a LinkedHashMap to include either a new key with values or adds values to a key
     *
     * @param updateMap is the map to be updated
     * @param key that is either being added with values or having values added to it
     * @param otherValues are the values being added
     * @return the updated map with the values and potentially new key added
     */
    private LinkedHashMap<String, ArrayList<String[]>> mapAdder(LinkedHashMap<String, ArrayList<String[]>> updateMap,
                                                                String key, String[] otherValues) {
        if (updateMap.containsKey(key)) {
            ArrayList<String[]> currentList = updateMap.get(key);
            boolean isRepeat = false;
            for (int i = 0; i < currentList.size(); i++) {
                String[] currentTuple = currentList.get(i);
                if (currentTuple[1].equals(otherValues[1])) {
                    isRepeat = true;
                }
            }
            if (!isRepeat) {
                currentList.add(otherValues);
            }
        } else {
            updateMap.put(key, new ArrayList<String[]>());
            updateMap.get(key).add(otherValues);
        }
        return updateMap;
    }

    /**
     * Parses the file and returns a LinkedHashMap of journals and String pairs of date and tweet
     *
     * @return the ArrayList of LinkedHashMaps
     * @throws FileNotFoundException 
     */
    public LinkedHashMap<String, ArrayList<String[]>> parse() throws FileNotFoundException {
    	System.out.println();
        LinkedHashMap<String, ArrayList<String[]>> journalMap = new LinkedHashMap<>();
        scan = new Scanner(System.in);
    	System.out.println("Please input your file: ");
    	String inputFile = scan.nextLine().trim();
    	File file = new File(inputFile);
        scan = new Scanner(file);
        while (scan.hasNextLine()) {
        	String currentLine = scan.nextLine();
        	String[] parsedLine = currentLine.split("//0,"); 
        	String date = parsedLine[0].substring(0, 10);
        	String journal = parsedLine[1];
        	String title = parsedLine[2];
        	journalMap = mapAdder(journalMap, journal, new String[]{date, title});
        }
        return journalMap;
    }

}
