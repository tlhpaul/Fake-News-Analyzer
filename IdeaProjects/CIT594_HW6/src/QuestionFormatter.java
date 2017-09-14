import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Updates a map, updates values in a map, filters a map and creates either a frequency or sentiment map
 */
public class QuestionFormatter {

    private final LinkedHashMap<String, ArrayList<String[]>> twitterMap;

    /**
     * Constructor for QuestionFormatter
     *
     * @param twitterMap is the twitterMap used in the QuestionFormatter
     */
    public QuestionFormatter(LinkedHashMap<String, ArrayList<String[]>> twitterMap) {
        this.twitterMap = twitterMap;
    }

    /**
     * Updates a linked map with a new value at a key
     *
     * @param value       is the count of number of values inserted into a key in the map
     * @param sumValues   is if the values should be summed for a key
     * @param identifier  is the key
     * @param keyMap      is the map being modified
     * @param allowSparse is if empty values should be added to the map
     * @return the updated linked map
     */
    private LinkedHashMap<String, ArrayList<Integer>> updateMap(int value, boolean sumValues, String identifier,
                                                                LinkedHashMap<String, ArrayList<Integer>> keyMap,
                                                                boolean allowSparse) {
        if (!allowSparse) {
            if (value != 0) {
                if (!sumValues) {
                    keyMap.get(identifier).add(value);
                } else {
                    keyMap = updateSumValue(identifier, value, keyMap);
                }
            }
        } else {
            if (!sumValues) {
                keyMap.get(identifier).add(value);
            } else {
                keyMap = updateSumValue(identifier, value, keyMap);
            }
        }
        return keyMap;
    }

    /**
     * Updates a key value pair by adding a new value to the current value
     *
     * @param identifier is the key
     * @param value      is the value being added to the current key's value
     * @param keyMap     is the map being modified
     * @return the modified linked map
     */
    private LinkedHashMap<String, ArrayList<Integer>> updateSumValue(String identifier, int value,
                                                                     LinkedHashMap<String, ArrayList<Integer>> keyMap) {

        if (keyMap.get(identifier).isEmpty()) {
            keyMap.get(identifier).add(value);
        } else {
            int aggregateCounter = keyMap.get(identifier).get(0);
            aggregateCounter += value;
            keyMap.get(identifier).remove(0);
            keyMap.get(identifier).add(aggregateCounter);
        }
        return keyMap;
    }

    /**
     * Filters on a key based on the question to be answered
     *
     * @param unfilteredList the original list of dates and tweets
     * @param primaryTerm    the primary term being searched on
     * @param positiveTerms  all positive terms to check
     * @param negativeTerms  all negative terms or null if not a sentiment analysis
     * @param sumValues      if values are being summed or listed
     * @param allowSparse    if the linked map allows zero values to be inserted
     * @param noFilter       if all tweets are examined or just the ones containing the primary term
     * @return a list of dates and counters for filtered tweets
     */
    private LinkedHashMap<String, ArrayList<Integer>> filterOnSentiment(ArrayList<String[]> unfilteredList,
                                                                        String primaryTerm,
                                                                        String[] positiveTerms, String[] negativeTerms,
                                                                        boolean sumValues, boolean allowSparse,
                                                                        boolean noFilter) {

        LinkedHashMap<String, ArrayList<Integer>> keyMap = new LinkedHashMap<>();
        ArrayList<String> goodTerms = new ArrayList<String>(Arrays.asList(positiveTerms));
        ArrayList<String> badTerms = new ArrayList<String>(Arrays.asList(negativeTerms));

        for (String[] entry : unfilteredList) {
            int counter = 0;
            String identifier = entry[0];
            String text = entry[1].toLowerCase();

            if (!keyMap.containsKey(identifier)) {
                keyMap.put(identifier, new ArrayList<Integer>());
            }

            if (text.contains(primaryTerm) || noFilter) {
                String[] wordList = text.split(" ");
                for (String word : wordList) {
                    String cleanedWord = word.replaceAll("/[^A-Za-z0-9 ]/", "").toLowerCase();

                    if (goodTerms.contains(cleanedWord)) {
                        counter += 1;
                    }
                    if (badTerms.contains(cleanedWord)) {
                        counter -= 1;
                    }
                }
            }
            keyMap = updateMap(counter, sumValues, identifier, keyMap, allowSparse);
        }
        return keyMap;
    }

    /**
     * Filters on a key based on the question to be answered
     *
     * @param unfilteredList the original list of dates and tweets
     * @param primaryTerm    the primary term being searched on
     * @param secondaryTerms all secondary terms to check
     * @param countInTweet   if frequency is counted on number of items in list
     *                       or words in item
     * @param sumValues      if values are being summed or listed
     * @param allowSparse    if the linked map allows zero values to be inserted
     * @param noFilter       if all tweets are examined or just the ones containing the primary term
     * @return a list of dates and counters for filtered tweets
     */
    private LinkedHashMap<String, ArrayList<Integer>> filterOnFrequency(ArrayList<String[]> unfilteredList,
                                                                        String primaryTerm, String[] secondaryTerms,
                                                                        boolean countInTweet, boolean sumValues,
                                                                        boolean allowSparse, boolean noFilter) {

        LinkedHashMap<String, ArrayList<Integer>> keyMap = new LinkedHashMap<>();
        ArrayList<String> allTerms = new ArrayList<String>(Arrays.asList(secondaryTerms));
        allTerms.add(primaryTerm);

        for (String[] entry : unfilteredList) {
            int counter = 0;
            String identifier = entry[0];
            String text = entry[1].toLowerCase();

            if (!keyMap.containsKey(identifier)) {
                keyMap.put(identifier, new ArrayList<Integer>());
            }

            if (text.contains(primaryTerm) || noFilter) {
                String[] wordList = text.split(" ");
                for (String word : wordList) {
                    if (countInTweet) {
                        String cleanedWord = word.replaceAll("/[^A-Za-z0-9 ]/", "").toLowerCase();
                        if (allTerms.contains(cleanedWord)) {
                            counter += 1;
                        }
                    } else {
                        counter += 1;
                    }
                }
                keyMap = updateMap(counter, sumValues, identifier, keyMap, allowSparse);
            }
        }
        return keyMap;
    }

    /**
     * Checks if a map never tweets
     *
     * @param map being checked
     * @return if the map ever tweets
     */
    private boolean mapNeverTweets(LinkedHashMap<String, ArrayList<Integer>> map) {

        for (String key : map.keySet()) {
            if (!map.get(key).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Asks question of listOfMaps and returns a filter map of maps on the sentiment question
     *
     * @param subset        of Maps to look in
     * @param primaryTerm   is the primary term being filtered on
     * @param terms         all primary terms or positive terms to check
     * @param negativeTerms all negative terms or null if not a sentiment analysis
     * @param sumValues     if values are being summed or listed
     * @param allowSparse   if the linked map allows zero values to be inserted
     * @param noFilter      if all tweets are examined or just the ones containing the primary term
     * @return the map of journals and their maps of dates with either a list or aggregated numerical answers
     */
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> askSentiment(ArrayList<String> subset,
                                                                                         String primaryTerm, String[] terms,
                                                                                         String[] negativeTerms,
                                                                                         boolean sumValues,
                                                                                         boolean allowSparse,
                                                                                         boolean noFilter) {

        LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> subMap = new LinkedHashMap<>();

        for (String sub : subset) {
            ArrayList<String[]> currentSub = twitterMap.get(sub);
            subMap.put(sub, filterOnSentiment(currentSub, primaryTerm, terms,
                    negativeTerms, sumValues, allowSparse, noFilter));

            if (mapNeverTweets(subMap.get(sub))) {
                subMap.remove(sub);
            }
        }
        return subMap;
    }

    /**
     * Asks question of listOfMaps and returns a filter map of maps on the frequency question
     *
     * @param subset         of Maps to look in
     * @param primaryTerm    is the primary term being filtered on
     * @param secondaryTerms all secondary terms to check
     * @param sumValues      if values are being summed or listed
     * @param countInTweet   if frequency is counted on number of tweets or words in tweets
     * @param allowSparse    if the linked map allows zero values to be inserted
     * @param noFilter       if all tweets are examined or just the ones containing the primary term
     * @return the map of journals and their maps of dates with either a list or aggregated numerical answers
     */
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> askFrequency(ArrayList<String> subset,
                                                                                         String primaryTerm,
                                                                                         String[] secondaryTerms,
                                                                                         boolean sumValues,
                                                                                         boolean countInTweet,
                                                                                         boolean allowSparse,
                                                                                         boolean noFilter) {

        LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> subMap = new LinkedHashMap<>();

        for (String sub : subset) {
            ArrayList<String[]> currentSub = twitterMap.get(sub);
            subMap.put(sub, filterOnFrequency(currentSub, primaryTerm, secondaryTerms,
                    countInTweet, sumValues, allowSparse, noFilter));

            if (mapNeverTweets(subMap.get(sub))) {
                subMap.remove(sub);
            }
        }
        return subMap;
    }

}