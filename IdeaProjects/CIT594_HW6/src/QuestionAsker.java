import java.util.*;

/**
 * Finds the subset being sought based on the user's question, finds keywords, makes map for question and then makes
 * statistical analysis if necessary and finally visualizes question with a graphic
 */
public class QuestionAsker {
    private MapAdapter mapAdapter = new MapAdapter();
    private VisualMaker visualMaker = new VisualMaker();
    private QuestionFormatter questionFormatter;
    private ArrayList<String> subset;
    private String grouping;
    private String xLabel = "day";

    /**
     * Constructor for QuestionAsker
     *
     * @param questionFormatter is the questionFormatter used to answer the question
     * @param grouping of media in question
     */
    public QuestionAsker(QuestionFormatter questionFormatter, String grouping) {

        this.questionFormatter = questionFormatter;
        this.grouping = grouping;
        this.subset = findSubset(grouping);
    }

    /**
     * Finds and returns all subsets
     *
     * @return all subsets
     */
    private ArrayList<String> findAllSubset() {
        ArrayList<String> allSubs = new ArrayList<>();
        allSubs.addAll(findSubset("left"));
        allSubs.addAll(findSubset("center"));
        allSubs.addAll(findSubset("right"));
        return allSubs;
    }

    /**
     * Finds the subset based a question grouping
     *
     * @param grouping that a question is specified with
     * @return the subset for the grouping
     */
    private ArrayList<String> findSubset(String grouping) {
        ArrayList<String> subset = new ArrayList<>();

        switch (grouping) {
            case "left":
                subset.add("HuffPost");
                subset.add("WashingtonPost");
                subset.add("NYT");
                subset.add("NBC");
                break;
            case "center":
                subset.add("CNN");
                subset.add("ABC");
                subset.add("WSJ");
                subset.add("bpolitics");
                break;
            case "right":
                subset.add("FoxNews");
                subset.add("TheBlaze");
                subset.add("amthinker");
                subset.add("realdonaldtrump");
                break;
            case "all":
                subset = findAllSubset();
                break;
        }
        return subset;
    }

    /**
     * Returns a list of primary and possibly secondary terms based on a question
     *
     * @param questionKey being used
     * @return the list of arrays of terms to be sought
     */
    private ArrayList<String[]> generateTerms(String questionKey) {

        ArrayList<String[]> termLists = new ArrayList<>();

        switch (questionKey) {
            case "sentiment":
                String[] positiveTerms = new String[]{"good", "great", "fine", "happy", "hopeful", "well", "succeed",
                        "winning"};
                String[] negativeTerms = new String[]{"bad", "fearful", "upset", "untrust", "worry",
                        "worried", "hate", "fail", "fake"};
                termLists.add(positiveTerms);
                termLists.add(negativeTerms);
                break;
            case "trump":
                positiveTerms = new String[]{"president", "potus", "donald"};
                termLists.add(positiveTerms);
                break;
            case "russia":
                positiveTerms = new String[]{"flynn", "putin", "carter page", "russian", "moscow"};
                termLists.add(positiveTerms);
                break;
            case "wiretap":
                positiveTerms = new String[]{"wiretapping", "wire", "tap", "tapped", "tapping", "obama"};
                termLists.add(positiveTerms);
                break;
            case "taxes":
                positiveTerms = new String[]{"tax", "cut", "decrease taxes"};
                termLists.add(positiveTerms);
                break;
            case "healthcare":
                positiveTerms = new String[]{"ahca", "aca", "preexisting", "trumpcare", "ryancare", "obamacare"};
                termLists.add(positiveTerms);
                break;
            case "budget":
                positiveTerms = new String[]{"balance", "cut", "spending"};
                termLists.add(positiveTerms);
                break;
            case "republican":
                positiveTerms = new String[]{"gop", "grand old party", "ryan", "mcconnell", "tea party",
                        "freedom caucus"};
                break;
            case "democrats":
                positiveTerms = new String[]{"dems", "democrats", "obama", "clinton", "maxine", "schumer", "pelosi"};
                termLists.add(positiveTerms);
                break;
            case "north korea":
                positiveTerms = new String[]{"kim jung", "korean"};
                termLists.add(positiveTerms);
            case "isis":
                positiveTerms = new String[]{"syria", "chemical", "terrorist", "terrorism", "bomb", "security"};
                termLists.add(positiveTerms);
                break;
            case "claim":
                termLists.add(generateTerms("russia").get(0));
                termLists.add(generateTerms("wiretap").get(0));
                break;
        }
        return termLists;
    }

    /**
     * Generates a sentiment map
     *
     * @param primaryTerm is the primary term being sought
     * @param allowSparse if the map can have values equal to zero
     * @param sentimentbyPositivity if the map is examining positivity or the relation of one question to another
     * @return the sentiment map
     */
    private LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> makeSentimentMap(String primaryTerm,
                                                                                              boolean allowSparse,
                                                                                              boolean sentimentbyPositivity) {
        ArrayList<String[]> termLists = new ArrayList<>();
        if (sentimentbyPositivity) {
            termLists = generateTerms("sentiment");
        } else {
            termLists = generateTerms(primaryTerm);
        }
        String[] positiveTerms = termLists.get(0);
        String[] negativeTerms = termLists.get(1);
        boolean noFilter = false;

        if (primaryTerm.equals("claim") || primaryTerm.equals("healthcare")) {
            noFilter = true;
        }

        return questionFormatter.askSentiment(subset, primaryTerm, positiveTerms, negativeTerms,
                false, allowSparse, noFilter);
    }

    /**
     * Generates a frequency map
     *
     * @param primaryTerm is the primary term being sought for
     * @param sumValues is if the values for each date are being summed
     * @param countInTweet if counting is by tweet or by words in tweet
     * @param allowSparse if values equal to zero are allowed
     * @return the frequency map
     */
    private LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> makeFrequencyMap(String primaryTerm, boolean
            sumValues, boolean countInTweet, boolean allowSparse) {

        ArrayList<String[]> termLists = generateTerms(primaryTerm);
        String[] terms = termLists.get(0);
        boolean noFilter = false;

        if (primaryTerm.equals("claim") || primaryTerm.equals("healthcare")) {
            noFilter = true;
        }

        return questionFormatter.askFrequency(subset, primaryTerm, terms, sumValues, countInTweet, allowSparse, noFilter);
    }

    /**
     * Finds a map for another group for the same question
     *
     * @param grouping is the group whose map is being sought
     * @param frequency is if the map will be on frequency or sentiment
     * @param sumValues is if the values at each date will be summed for a media source
     * @param countInTweet if count will be on tweet
     * @param primaryTerm is the primary term being sought
     * @param allowSparse if values equal to zero are allowed
     * @param noFilter if filtering is on primary term or all tweets are examined regardless of containing primary term
     * @return the map for the other group
     */
    private LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> mapForOther(String grouping, boolean frequency,
                                                                                         boolean sumValues, boolean countInTweet,
                                                                                         String primaryTerm, boolean allowSparse,
                                                                                         boolean noFilter) {

        ArrayList<String[]> termLists = generateTerms(primaryTerm);
        ArrayList<String> subset = findSubset(grouping);

        if (frequency) {
            return questionFormatter.askFrequency(subset,
                    primaryTerm, termLists.get(0), sumValues, countInTweet, allowSparse, noFilter);
        } else {
            return questionFormatter.askSentiment(subset,
                    primaryTerm, termLists.get(0), termLists.get(1), sumValues, allowSparse, noFilter);
        }
    }


    /**
     * Creates a scatter plot showing media attention to Trump with positive and negative words
     */
    public void questionOne() {

        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> sentimentMap = makeSentimentMap(
                "trump", false, true);

        final ArrayList<ArrayList<Integer[]>> coordinateList = mapAdapter.convertToScatterPlot(sentimentMap);

        visualMaker.scatterPlotMaker("How kind news been to Trump", subset, xLabel, "sentiment",
                coordinateList);
    }

    /**
     * Creates a comparative  bar chart of media vs. right wing media mentioning of taxes
     */
    public void questionTwo() {

        final String questionKey = "taxes";

        ArrayList<String> groups = new ArrayList<>();
        groups.add("right");

        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> trumpMention = makeFrequencyMap(questionKey,
                false, true, false);

        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> rightMention = mapForOther("right",
                true, false, true, questionKey, false, false);

        final ArrayList<Integer> coordinateList = mapAdapter.sumAggregatedMap(trumpMention);
        final ArrayList<Integer> coordinateListRight = mapAdapter.sumAggregatedMap(rightMention);
        ArrayList<ArrayList<Integer>> expandedList = new ArrayList<>();
        expandedList.add(coordinateList);
        expandedList.add(coordinateListRight);

        visualMaker.barGraphMaker("Frequency that " + grouping + " vs. right mentioned Taxes", groups, xLabel,
                "frequency", expandedList);
    }

    /**
     * The rate that the news has stopped mentioning Russia via a decomposition trend
     */
    public void questionThree() {

        final String questionKey = "russia";

        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> russiaMention = makeFrequencyMap(questionKey,
                true, true, false);

        final LinkedHashMap<String, ArrayList<Integer[]>> decompositionList = new StatisticalAdpater().getDecomposition(russiaMention,
                5, false);

        final ArrayList<Integer> decompositionArray = mapAdapter.covertArrayMap(decompositionList);

        visualMaker.lineChartMaker("Rate that the news decreased mentioning Russia", grouping, xLabel,
                "frequency", decompositionArray);
    }

    /**
     * Creates a linear regression of wiretap mentions over time
     */
    public void questionFour(){
       final String questionKey = "wiretap";

        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> wireMention = makeFrequencyMap(questionKey,
                true, true, false);

        final LinkedHashMap<String, ArrayList<Integer[]>> regressionList = new StatisticalAdpater().getBasicStatisitcsFunction(
                wireMention, 1, 0);

        final ArrayList<Integer> regressionArray = mapAdapter.covertArrayMap(regressionList);

        visualMaker.lineChartMaker("The rate that news talked about wiretapping", grouping, xLabel,
                "frequency", regressionArray);
    }

    /**
     * Creates a moving trend line for media mention of trump
     */
    public void questionFive(){
        final String questionKey = "trump";

        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> wireMention = makeFrequencyMap(questionKey,
                false, true, false);

        final LinkedHashMap<String, ArrayList<Integer[]>> trendMap = new StatisticalAdpater().getBasicStatisitcsFunction(
                wireMention, 2, 0);

        final  ArrayList<Integer> trendArray = mapAdapter.covertArrayMap(trendMap);

        visualMaker.lineChartMaker("The moving average for media discussion of trump", grouping, xLabel,
                "frequency", trendArray);
    }

    /**
     * creates a multiline analysis for mentions of democrats by all media sources
     */
    public void questionSix(){
        final String questionKey = "democrats";

        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> democratsMention =
                makeFrequencyMap(questionKey, true, true, true);
        final ArrayList<ArrayList<ArrayList<Integer>>> values = mapAdapter.adaptForMultiLine(democratsMention);
        final ArrayList<String> titles = mapAdapter.getTitlesForMultiLine(democratsMention);
        visualMaker.multiLineMaker("News outlet mentions of Democrats over time", xLabel, "Counts",
                values, titles);
    }

    /**
     * creates a scatter plot for media mentions of North Korea
     */
    public void questionSeven(){
        final String questionKey = "north korea";

        final  LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> northKoreaMention =
                makeFrequencyMap(questionKey, true, true, false);

        final ArrayList<ArrayList<Integer[]>> coordinateList = mapAdapter.convertToScatterPlot(northKoreaMention);

        visualMaker.scatterPlotMaker("News outlet mentions of North Korea over time", subset, xLabel,
                "Counts", coordinateList);
    }

    /**
     * Creates a scatter plot of multivariate regression of time and political leaning on mentions of healthcare
     */
    public void questionEight() {
        final String questionKey = "healthcare";

        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> healthcareMap = makeFrequencyMap(questionKey,
                true, false, true);
        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> convertedMap = mapAdapter.convertBySpectrum(healthcareMap);
        final ArrayList<ArrayList<Integer>> statisticalList = new StatisticalAdpater().getMultiRegression(convertedMap);
        ArrayList<String> subSet = new ArrayList<>();
        for (int i = 1; i < 155; i++) {
            subSet.add(Integer.toString(i));
        }
        final ArrayList<ArrayList<Integer[]>> map = mapAdapter.convertMultivariateToScatterPlot(statisticalList);
        visualMaker.scatterPlotMaker("The Trend Of Frequency Mentioning Healthcare", subSet,
                "political leaning", "Frequency", map);
    }

    /**
     * Creates a multiline chart of outlet mentions of ISIS
     */
    public void questionNine() {
        final String questionKey = "isis";

        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> ISISMention =
                makeFrequencyMap(questionKey, true, true, true);
        final ArrayList<ArrayList<ArrayList<Integer>>> values = mapAdapter.adaptForMultiLine(ISISMention);
        final ArrayList<String> titles = mapAdapter.getTitlesForMultiLine(ISISMention);
        visualMaker.multiLineMaker("News outlet mentions of ISIS and security over time", xLabel, "Counts",
                values, titles);
    }

    /**
     * Creates a bar chart that shows the ratio of media mentions of Russia to Wiretapping over time
     */
    public void questionTen() {
        final String questionKey = "claim";
        ArrayList<ArrayList<Integer>> expandedList = new ArrayList<>();

        ArrayList<String> groups = new ArrayList<>();
        for (String source : subset) {
            groups.add(source);
        }

        final LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> conflictMention = makeSentimentMap(questionKey,
                false, false);

        for (String key : conflictMention.keySet()) {
            expandedList.add(mapAdapter.aggregateMap(conflictMention.get(key)));
        }

        visualMaker.barGraphMaker("Frequency that all mentioned Russia vs. wiretapping", groups, xLabel,
                "frequency", expandedList);
    }
}
