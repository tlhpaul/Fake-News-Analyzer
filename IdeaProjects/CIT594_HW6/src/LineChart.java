import java.awt.*;
import java.util.ArrayList;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * Creates a line chart, fills data and populates the chart
 *
 */
public class LineChart extends ApplicationFrame implements LinearChart {

    private static final long serialVersionUID = 1L;

    static {
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",
                true));
    }

    /**
     * Creates the line chart
     *
     * @param title the frame title.
     * @param subtitle the subtitle 
     * @param xLabel sets the lables for X coordinate 
     * @param values the dataset with values 
     * @param comparator journals to compare
     */
    public LineChart(String title, String subtitle, String xLabel, ArrayList<ArrayList<Integer>> values,
                     String comparator) {

        super(title);
        ArrayList<String> expandedComparator = new ArrayList<>();
        expandedComparator.add(comparator);
        CategoryDataset dataset = LinearChart.createDataset(values, expandedComparator);
        JFreeChart chart = createChart(dataset, title, subtitle, xLabel);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setBackground(null);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setDismissDelay(Integer.MAX_VALUE);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }


    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset, String title, String categoryAxis,
                                          String valueAxis) {
        JFreeChart lineChartObject = ChartFactory.createLineChart(
                title,categoryAxis,
                valueAxis,
                dataset,PlotOrientation.VERTICAL,
                true,true,false);

        return lineChartObject;
    }

}