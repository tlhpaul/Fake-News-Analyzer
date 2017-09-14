import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Transforms maps into aggregated maps, coordinate arraylists, flat arraylists, adapted lists for multiline,
 * scatter plots or spectrum analyses
 */
public class MapAdapter {

    /**
     * Aggregates a map of Maps
     *
     * @param mapOfMaps is the map of maps to be aggregated into a single map
     * @return the aggregate map of a map of maps
     */
    private LinkedHashMap<String, ArrayList<Integer>> aggregateMapofMaps(
            LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> mapOfMaps) {

        LinkedHashMap<String, ArrayList<Integer>> aggregatedMap = new LinkedHashMap<>();

        for (String map : mapOfMaps.keySet()) {
            LinkedHashMap<String, ArrayList<Integer>> currentMap = mapOfMaps.get(map);

            for (String identifier : currentMap.keySet()) {
                if (!aggregatedMap.containsKey(identifier)) {
                    aggregatedMap.put(identifier, new ArrayList<Integer>());
                }
                aggregatedMap.get(identifier).addAll(currentMap.get(identifier));
            }
        }
        return aggregatedMap;
    }

    /**
     * Converts a map into an Arraylist of coordinates
     *
     * @param map to be converted
     * @return the coordinate Arraylist
     */
    public ArrayList<Integer[]> convertMap(LinkedHashMap<String, ArrayList<Integer>> map) {

        ArrayList<Integer[]> coordinateList = new ArrayList<>();
        Integer counter = 0;
        for (String key : map.keySet()) {
            Integer x = counter;
            for (Integer i = 0; i < map.get(key).size(); i++) {
                Integer y = map.get(key).get(i);
                coordinateList.add(new Integer[]{x, y});
            }
            counter++;
        }
        return coordinateList;
    }

    /**
     * Aggregates a map into a flat Arraylist
     *
     * @param map to be aggregated
     * @return flat Arraylist
     */
    public ArrayList<Integer> aggregateMap(LinkedHashMap<String, ArrayList<Integer>> map) {
        ArrayList<Integer> aggregatedList = new ArrayList<>();

        for (String key : map.keySet()) {
            int coordinateCount = 0;
            for (Integer value : map.get(key)) {
                coordinateCount += value;
            }
            aggregatedList.add(coordinateCount);
        }
        return aggregatedList;
    }

    /**
     * Transforms a map of maps into an aggregated flat Arraylist
     *
     * @param questionMap is the map of maps to be aggregated
     * @return the flattened Arraylist
     */
    public ArrayList<Integer> sumAggregatedMap(LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> questionMap) {
        LinkedHashMap<String, ArrayList<Integer>> aggregatedMap = aggregateMapofMaps(questionMap);

        ArrayList<ArrayList<Integer>> coordinateList = new ArrayList<>();
        ArrayList<Integer> compressedList = new ArrayList<>();

        for (String key : aggregatedMap.keySet()) {
            coordinateList.add(aggregatedMap.get(key));
        }

        for (ArrayList<Integer> currentList : coordinateList) {
            int sum = currentList.stream().mapToInt(Integer::intValue).sum();
            compressedList.add(sum);
        }
        return compressedList;
    }

    /**
     * Changes LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> to
     * ArrayList<ArrayList<ArrayList<Integer>>> for multiLine chart
     *
     * @param questionMap
     * @return
     */
    public ArrayList<ArrayList<ArrayList<Integer>>> adaptForMultiLine(
            LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> questionMap) {
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Integer>>> values = new ArrayList<ArrayList<ArrayList<Integer>>>();
        for (Map.Entry<String, LinkedHashMap<String, ArrayList<Integer>>> entry1 : questionMap.entrySet()) {
            String key = entry1.getKey();
            LinkedHashMap<String, ArrayList<Integer>> subMap = entry1.getValue();
            ArrayList<ArrayList<Integer>> value2 = new ArrayList<ArrayList<Integer>>();
            for (Map.Entry<String, ArrayList<Integer>> entry2 : subMap.entrySet()) {
                ArrayList<Integer> value = entry2.getValue();
                ArrayList<Integer> valueToInput = new ArrayList<Integer>();
                for (int i = 0; i < value.size(); i++) {
                    valueToInput.add(value.get(i));
                }
                value2.add(valueToInput);
            }
            values.add(value2);
            titles.add(key);
        }
        return values;
    }

    /**
     * Gets the title for multiline chart
     *
     * @param questionMap
     * @return
     */
    public ArrayList<String> getTitlesForMultiLine(LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> questionMap) {
        ArrayList<String> titles = new ArrayList<>();
        for (Map.Entry<String, LinkedHashMap<String, ArrayList<Integer>>> entry1 : questionMap.entrySet()) {
            String key = entry1.getKey();
            titles.add(key);
        }
        return titles;
    }


    /**
     * Converts a coordinate map to a flat Arraylist
     *
     * @param reversibleMap
     * @return
     */
    public ArrayList<Integer> covertArrayMap(LinkedHashMap<String, ArrayList<Integer[]>> reversibleMap) {

        ArrayList<Integer> results = new ArrayList<Integer>();
        for (String key : reversibleMap.keySet()) {
            for (Integer[] list : reversibleMap.get(key)) {
                if (results.size() > list[0]) {
                    @SuppressWarnings("unused")
					int count = results.get(list[0]);
                    count += list[1];
                } else {
                    results.add(list[1]);
                }
            }
        }
        return results;
    }

    /**
     * Converts a map of maps to a scatter plot viable data set
     *
     * @param map to be transformed
     * @return the Arraylist of coordinates to be used in a scatter plot
     */
    public ArrayList<ArrayList<Integer[]>> convertToScatterPlot(
            LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> map) {

        ArrayList<ArrayList<Integer[]>> coordinateList = new ArrayList<>();
        for (String singleKey : map.keySet()) {
            ArrayList<Integer[]> coordinateSubList = convertMap(map.get(singleKey));
            coordinateList.add(coordinateSubList);
        }
        return coordinateList;
    }

    /**
     * Converts multivariate dataset to data viable in a scatter plot
     *
     * @param multivariateData to be transformed
     * @return the transformed dataset for use in a scatter plot
     */
    public ArrayList<ArrayList<Integer[]>> convertMultivariateToScatterPlot(
            ArrayList<ArrayList<Integer>> multivariateData) {

        ArrayList<ArrayList<Integer[]>> coordinateList = new ArrayList<ArrayList<Integer[]>>();

        for (int i = 0; i < multivariateData.size(); i++) {
            ArrayList<Integer[]> temp = new ArrayList<Integer[]>();
            temp.add(new Integer[]{multivariateData.get(i).get(0), multivariateData.get(i).get(2)});
            coordinateList.add(temp);
        }
        return coordinateList;
    }

    /**
     * Converts tweeter map by political leaning to left, right and center, and 
     * sum up the thre frequency 
     * @param map LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>>
     * @return results of LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>>
     */
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> convertBySpectrum(
            LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> map) {
    	String[] leftOptions = new String[] {"HuffPost", "WashingtonPost", "NYT", "NBC"};
    	String[] centerOptions = new String[] {"CNN", "ABC", "WSJ", "bpolitics"};
        LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> newMap = new
                LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>>();
        for (String key : map.keySet()) {
            if (Arrays.asList(leftOptions).contains(key)) {
                if (!newMap.containsKey("left")) {
                    newMap.put("left", map.get(key));
                } else {
                	newMap = getMap("left", key, newMap, map);
                }
            } else if (Arrays.asList(centerOptions).contains(key)) {
                if (!newMap.containsKey("center")) {
                    newMap.put("center", map.get(key));
                } else {
                	newMap = getMap("center", key, newMap, map);
                }
            } else {
                if (!newMap.containsKey("right")) {
                    newMap.put("right", map.get(key));
                } else {
                	newMap = getMap("right", key, newMap, map);
                }
            }
        }
        return newMap;
    }
    
    /**
     * Sums the value in arraylist up by its key in outer hashmap
     * @param leaning political left, right and center
     * @param key Journals
     * @param newMap LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> to return
     * @param map LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> input in
     * @return results of LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>>
     */
    private LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> getMap(String leaning, String key, 
    		LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> newMap, LinkedHashMap<String, 
    		LinkedHashMap<String, ArrayList<Integer>>> map) {
    	 LinkedHashMap<String, ArrayList<Integer>> newSubMap = newMap.get(leaning);
         LinkedHashMap<String, ArrayList<Integer>> subMap = map.get(key);
         for (String date : subMap.keySet()) {
             if (newSubMap.containsKey(key)) {
                 ArrayList<Integer> sumList = newSubMap.get(date);
                 ArrayList<Integer> countList = subMap.get(date);
                 for (Integer sum : sumList) {
                     for (Integer count : countList) {
                    	 sum += count;
                     }
                 }
             } else {
                 newSubMap.put(date, subMap.get(date));
             }
         }
         return newMap;
    }
}
