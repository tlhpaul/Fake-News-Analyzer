import java.util.ArrayList;

import Jama.Matrix;
import Jama.QRDecomposition;

/**
 * Advanced Statistics class to compute the 
 * multivariable linear regression
 * 
 */
public class AdvancedStatistics {
    private Matrix beta;  
    private double[][] x;
    private double[] y;
    private double avgY;

    /**
     * Constructor for Advanced Statistics class
     * @param x X values
     * @param y Y values
     */
    public AdvancedStatistics(double[][] x, double[] y){
        this.x = x;
        this.y = y;
    }

    /**
     * The main method to compute multivariable linear regression,
     * uses matrix to calculate beta and average of y
     */
    private void multiVariableRegression() {
        if (x.length != y.length) throw new RuntimeException("input value is incorrect");
        int numberOfY = y.length;
        Matrix X = new Matrix(x);
        Matrix Y = new Matrix(y, numberOfY);
        QRDecomposition QR = new QRDecomposition(X);
        beta = QR.solve(Y);
        double sum = 0.0;
        for (int i = 0; i < numberOfY; i++){
            sum += y[i];
        }
        avgY = sum / numberOfY;
    }
    
    /**
     * Get regression coefficient for different X
     * @param j indicates which regression coefficient want to get
     * @return regression coefficient
     */
    private double beta(int j) {
        return beta.get(j, 0);
    }

    /**
     * Gets X and Y value after computing multivariable linear regression
     * @return An ArrayList<ArrayList<Integer>>, the last number is Y and all numbers before Y are different X value
     */
    public ArrayList<ArrayList<Integer>> getMultiRegressionResults() {
        multiVariableRegression();
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < x.length; i ++) {
            results.add(new ArrayList<Integer>());
            ArrayList<Integer> temp = results.get(i);
            double y = calculateIntercept();
            for (int j = 0; j < x[0].length; j++) {
                double tempX = x[i][j];
                temp.add((int) tempX);
                y += beta(j)*tempX;
            }
            temp.add((int) y);
        }
        return results;
    }

    /**
     * Calcaultes the intercept in formula of multivariable linear regression
     * @return intercept
     */
    private double calculateIntercept() {
        double intercept = avgY;
        ArrayList<Double> avgList = calculateXAvg();
        for (int i = 0; i < avgList.size(); i++) {
            intercept += avgList.get(i)*beta(i);
        }
        return intercept;
    }

    /**
     * Calculates average of array of X
     * @return average of array of X
     */
    private ArrayList<Double> calculateXAvg() {
        int sampleOfX = x.length;
        ArrayList<Double> avgList = new ArrayList<Double>();
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j ++) {
                if (i == 0) {
                    avgList.add(x[i][j]);
                } else {
                    @SuppressWarnings("unused")
                    double avg = avgList.get(j);
                    avg += x[i][j];
                }
            }
        }
        for (int i = 0; i < avgList.size(); i ++) {
            double avg = avgList.get(i);
            avg = avg/sampleOfX;
        }
        return avgList;
    }
}