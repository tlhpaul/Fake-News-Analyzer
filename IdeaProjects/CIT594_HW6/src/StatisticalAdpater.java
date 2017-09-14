import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Statistical Adapter class, returns multivariable linear regression in AdvancedStatistics class, 
 * and linear regression, trendline and moving average in BasicStatistics class
 *
 */
public class StatisticalAdpater {
	private AdvancedStatistics advancedStatistics;
	
	/**
	 * Gets results from multivariable linear regression in AdvancedStatistics class
	 * @param map LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>>
	 * @return results of ArrayList<ArrayList<Integer>>
	 */
    public ArrayList<ArrayList<Integer>> getMultiRegression(LinkedHashMap<String, LinkedHashMap<String, 
    		ArrayList<Integer>>> map) {
    	double [][] x;
    	double[] y;
    	ArrayList<Integer> tempY = new ArrayList<Integer>();
    	ArrayList<double[]> tempX = new ArrayList<double[]>() ;
    	for (String journal: map.keySet()) {
    		LinkedHashMap<String, ArrayList<Integer>> tempMap = map.get(journal);
			int size = tempMap.size() -1;
			for (String date: tempMap.keySet()) {
				for(Integer count: tempMap.get(date)) {
					tempY.add(count);
					if (journal.equals("left")) {
						tempX.add(new double[] {0, size});
					} else if (journal.equals("center")) {
						tempX.add(new double[] {1, size});
					} else {
						tempX.add(new double[] {2, size});
					}
					size --;
				}
			}
    	}
    	 y = populateY(tempY);
    	 x = populateX(tempX);
    	advancedStatistics = new AdvancedStatistics(x, y);
    	return advancedStatistics.getMultiRegressionResults();
    }
    
    /**
     * Converts LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> to LinkedHashMap<String, BasicStatistics> 
     * for BasicStatistics class
     * @param map
     * @return
     */
    private LinkedHashMap<String, BasicStatistics> convertQuestionMapToBasic(LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>> map) {
    	LinkedHashMap<String, BasicStatistics> basicStatisticsMap = new LinkedHashMap<String, BasicStatistics>();
    	for (Map.Entry<String, LinkedHashMap<String, ArrayList<Integer>>> entry1 : map.entrySet()) {
    		ArrayList<Integer[]> valueList = new ArrayList<Integer[]>();
    		String key = entry1.getKey();
    		LinkedHashMap<String, ArrayList<Integer>> value = entry1.getValue();
    		int index = 0;
    	    for (Map.Entry<String, ArrayList<Integer>> subEntry1: value.entrySet()) {
    	    	ArrayList<Integer> subList = subEntry1.getValue();
    	    	for (int i = 0; i < subList.size(); i++) {
    	    		valueList.add(new Integer[] {index, subList.get(i)});
    	    	}
    	    	index ++;
    	    }
    	    BasicStatistics basicStatistics = basicStatisticsMap.get(key);
    	    basicStatistics = new BasicStatistics(valueList);
    	    basicStatisticsMap.put(key, basicStatistics);
    	}
    	return basicStatisticsMap;
    }
    
    
    /**
     * Gets map of linear regression, trendline and movingAverage
     * @param map LinkedHashMap<String, LinkedHashMap<String, ArrayList<Integer>>>
     * @param input 1 to get linear regression, 2 to get trendline, 3 to get movingAverage
     * @return
     */
    public LinkedHashMap<String, ArrayList<Integer[]>> getBasicStatisitcsFunction(LinkedHashMap<String, 
    		LinkedHashMap<String, ArrayList<Integer>>> map, int input, int timeSpan) {
    	LinkedHashMap<String, BasicStatistics> mapOfBasicStatistics = convertQuestionMapToBasic(map); 
    	LinkedHashMap<String, ArrayList<Integer[]>> results = new LinkedHashMap<String, ArrayList<Integer[]>>();
    	for (Map.Entry<String, BasicStatistics> entry: mapOfBasicStatistics.entrySet()) {
    		String key = entry.getKey();
    		switch (input) {
    		case 1:
    			ArrayList<Integer[]> value1 = entry.getValue().linearRegression();
        		results.put(key, value1);
    			break;
    		case 2:
    			ArrayList<Integer[]> value2 = entry.getValue().trendLine();
    			results.put(key, value2);
    			break;
    		case 3:
    			ArrayList<Integer[]> value3 = entry.getValue().movingAverage(timeSpan);
    			results.put(key, value3);
    			break;
    		}
    	}
    	return results;
    }
    
    /**
     * Gets map of decomposition
     * @param map
     * @param timeSpan
     * @param isAdditive
     * @return
     */
    public LinkedHashMap<String, ArrayList<Integer[]>> getDecomposition(LinkedHashMap<String,
			LinkedHashMap<String, ArrayList<Integer>>> map, int timeSpan, boolean isAdditive) {
    	LinkedHashMap<String, BasicStatistics> mapOfBasicStatistics = convertQuestionMapToBasic(map); 
    	LinkedHashMap<String, ArrayList<Integer[]>> results = new LinkedHashMap<String, ArrayList<Integer[]>>();
    	for (Map.Entry<String, BasicStatistics> entry: mapOfBasicStatistics.entrySet()) {
    		String key = entry.getKey();
    		ArrayList<Integer[]> value = entry.getValue().decomposition(timeSpan, isAdditive);
			results.put(key, value);
    	}
    	return results;
    }
    
    /**
     * Populates double [][] X for AdvancedStatistics class 
     * @param ListX ArrayList<double[]>
     * @return double [][] after populating
     */
    private double [][] populateX (ArrayList<double[]> ListX) {
    	double [][] x = new double [ListX.size()][];
	   	 for (int i = 0; i < ListX.size(); i ++) {
	   		 x[i] = ListX.get(i);
	   	 }
	   	 return x;
    }
    
    /**
     * Populates double [] Y for AdvancedStatistics class 
     * @param ListY ArrayList<Integer>
     * @return double [] after populating
     */
    private double [] populateY (ArrayList<Integer> ListY) {
    	double [] y = new double[ListY.size()];
	   	for (int i = 0; i < ListY.size(); i ++) {
	   		 y[i] = ListY.get(i);
	   	}
	   	return y;
    }
}