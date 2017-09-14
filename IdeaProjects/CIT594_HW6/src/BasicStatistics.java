import java.util.ArrayList;

/**
 * Basic Statistics class, computing linear regression, 
 * trendline and moving average 
 *
 */
public class BasicStatistics {

    private ArrayList<Integer[]> valueList;

    /**
     * Basic Statistics constructor 
     * @param valueList
     */
    public BasicStatistics(ArrayList<Integer[]> valueList){
        this.valueList = valueList;
    }

    /**
     * Makes the sparse list 
     * @return ArrayList<Integer[]> with values 
     */
    private ArrayList<Integer[]> makeSparseList(){
        ArrayList<Integer[]> sparseList = new ArrayList<>();
        for (Integer[] coordinates: valueList) {
            int x = coordinates[0];
            if(sparseList.size() < x){
                for (int i = sparseList.size(); i < x; i++) {
                    sparseList.add(new Integer[]{0, 0});
                }
                sparseList.add(coordinates);
            }
        }
        return sparseList;
    }
    
    /**
     * Computes statistical linear regression 
     * @return An arrayList<Integer[]> with values computed by linear regression 
     */
    public ArrayList<Integer[]> linearRegression() {
        ArrayList<Integer[]> regressionList =  new ArrayList<>();
        double sigmaX = 0.0, sigmaY = 0.0, sigmaXY = 0.0, sigmaX2 = 0.0;
        for(Integer[] pair: valueList){
            Integer x = pair[0];
            Integer y = pair[1];
            sigmaX += x;
            sigmaY += y;
            sigmaX2 += x*x;
            sigmaXY += x*y;
        }
        int n = valueList.size();
        double a = ((sigmaY*sigmaX2) - (sigmaX*sigmaXY))/((n*sigmaX2) - (sigmaX*sigmaX)); 
        double b = ((n*sigmaXY) - (sigmaX*sigmaY))/((n*sigmaX2) - (sigmaX*sigmaX));
        for(Integer[] pair: valueList){
            Integer X = pair[0];
            Integer Y = (int) (a + b*X);
            regressionList.add(new Integer[] {X, Y});
        }
        return regressionList;
    }

    /**
     * Computes statistical trendline 
     * @return An arrayList<Integer[]> with values computed by trendline
     */
    public ArrayList<Integer[]> trendLine(){
        ArrayList<Integer[]> trendList = new ArrayList<>();
        ArrayList<Integer[]> sparseList = makeSparseList();
        int n = sparseList.size();
        if(n == 1){
            return new ArrayList<Integer[]>();
        }
        Double a = 0.0, b = 0.0, c = 0.0, d = 0.0, e = 0.0, f = 0.0, pointCalculation = 0.0;
        Double xSum = 0.0, ySum = 0.0, xSquaredSum = 0.0;
        for(Integer[] coordinate: sparseList){
            pointCalculation += coordinate[0] * coordinate[1];
            xSum += coordinate[0];
            ySum += coordinate[1];
            xSquaredSum += coordinate[0]^2;
        }
        a = pointCalculation * n;
        b = xSum * ySum;
        c = xSquaredSum * n;
        d = xSum*xSum;
        Double slope = (a - b)/(c - d);
        e = ySum;
        f = slope * xSum;
        Double intercept = (e - f)/n;
        for(Integer[] coordinate: sparseList){
            int x = coordinate[0];
            Double trendValue = slope * x + intercept;
            trendList.add(new Integer[]{x, trendValue.intValue()});
        }
        return trendList;
    }

    /**
     * Calculates the moving average at every point
     *
     * @param timeSpan is the number of days used to calculate into the future from a given point
     * @return a list of dates and moving averages corresponding to each day
     */
    public ArrayList<Integer[]> movingAverage(int timeSpan) {
        ArrayList<Integer[]> movingAverageList = new ArrayList<>();
        Queue<Integer> timeQueue = new Queue<Integer>();
        for (int i = 0; i < timeSpan; i++) {
            timeQueue.add(new Node<Integer>(valueList.get(i)[1]));
        }
        for (Integer[] item: valueList) {
            int increase = 0, queueIndex = 0, sum = 0;
             while (queueIndex < timeSpan) {
                sum += (Integer) timeQueue.indexAt(queueIndex).getContents();
                queueIndex ++;
            }
            movingAverageList.add(new Integer[]{item[0], sum/queueIndex});
            increase ++;
            timeQueue.delete();
            if (timeSpan + increase < valueList.size()) {
                timeQueue.add(new Node<Integer>(valueList.get(timeSpan + increase)[1]));
            } else {
                break;
            }
        }
        return movingAverageList;
    }

    /**
     * Deconstructs the time series from tweets into multiple components 
     * @param timeSpan is the number of days used to calculate into the future from a given point
     * @param isAdditive Checks whether it is additive in decomposition 
     * @return an ArrayList<Integer[]> with values computed by decomposition
     */
    public ArrayList<Integer[]> decomposition(int timeSpan, boolean isAdditive) {
        ArrayList<Integer[]> movingAverageList = movingAverage(timeSpan);
        ArrayList<Integer[]> smoothedList = new ArrayList<>();
        for (int i = 0; i < movingAverageList.size(); i++) {
            Integer series = movingAverageList.get(i)[0];
            Integer trend = movingAverageList.get(i)[1];
            if (trend != 0) {
            	if (isAdditive) {
                    smoothedList.add(new Integer[]{series, series - trend});
                } else {
                    smoothedList.add(new Integer[]{series, series / trend});
                }
            } else {
            	System.out.println("Trend is 0.");
            	break;
            }	
        }
        return smoothedList;
    }

}
