import org.jfree.ui.RefineryUtilities;

import java.util.ArrayList;

/**
 * Generates the visual structures that make up our view
 *
 */
public class VisualMaker{

    /**
     * Generates a visual bar graph
     *
     * @param title for the graph
     * @param compareList Arraylist of groups being compared
     * @param xLabel is the label for the x axis
     * @param yLabel is the label for the y axis
     * @param values is the list of lists of values being evaluated
     */
    public void barGraphMaker(String title, ArrayList<String> compareList, String xLabel, String yLabel,
                              ArrayList<ArrayList<Integer>> values) {

        if(values.size() == 0){
            System.out.println("There were zero occurrences");
            return;
        }
        BarChart chart = new BarChart(title, xLabel, yLabel, values, compareList);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

    /**
     * Generates a visual line chart
     *
     * @param title for the graph
     * @param compareList Arraylist of groups being compared
     * @param xLabel is the label for the x axis
     * @param yLabel is the label for the y axis
     * @param values is the list of values being evaluated
     */
    public void lineChartMaker(String title, String compareList, String xLabel, String yLabel,
                               ArrayList<Integer> values) {

        ArrayList<ArrayList<Integer>> expandedList = new ArrayList<>();
        expandedList.add(values);
        if(values.size() == 0){
            System.out.println("There were zero occurrences");
            return;
        }
        LineChart chart = new LineChart(title, xLabel, yLabel, expandedList, compareList);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

    /**
     * Generates a visual scatter plot
     *
     * @param title for the graph
     * @param compareList Arraylist of groups being compared
     * @param xLabel is the label for the x axis
     * @param yLabel is the label for the y axis
     * @param values is the list of xy values being evaluated
     */
    public void scatterPlotMaker(String title, ArrayList<String> compareList, String xLabel, String yLabel,
                                  ArrayList<ArrayList<Integer[]>> values){

         if(values.size() == 0){
             System.out.println("There were zero occurrences");
             return;
         }
        ScatterPlotChart chart = new ScatterPlotChart(title, compareList, xLabel, yLabel, values);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

    /**
     * Generates a visual multiline chart
     *
     * @param title for the graph
     * @param xLabel is the label for the x axis
     * @param yLabel is the label for the y axis
     * @param values is the list of lists of lists of values being evaluated
     * @param compareList Arraylist of groups being compared

     */
    public void multiLineMaker(String title, String xLabel, String yLabel,
                               ArrayList<ArrayList<ArrayList<Integer>>> values, ArrayList<String> compareList){
    	if(values.size() == 0){
            System.out.println("There were zero occurrences");
            return;
        }
        MultiLineChart chart = new MultiLineChart(title, xLabel, yLabel, values, compareList);
        chart.pack( );
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible( true );
    }

}
