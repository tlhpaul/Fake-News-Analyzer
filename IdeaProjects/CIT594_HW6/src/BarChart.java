import java.awt.Dimension;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * Creates a bar chart, fills data and populates the chart
 *
 */
public class BarChart extends ApplicationFrame implements LinearChart {

    private static final long serialVersionUID = 1L;

	static {
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",
                false));
    }

    /**
     * 
     * Creates a new bar chart.
     *  
     * @param title the frame title.
     * @param subtitle the subtitle 
     * @param xLabel sets the lables for X coordinate 
     * @param values the dataset with values 
     * @param comparator journals to compare
     */
    public BarChart(String title, String subtitle, String xLabel, ArrayList<ArrayList<Integer>> values,
                    ArrayList<String> comparator) {

        super(title);
        CategoryDataset dataset = LinearChart.createDataset(values, comparator);
        JFreeChart chart = createChart(dataset, title, subtitle, xLabel, comparator);
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
    private static JFreeChart createChart(CategoryDataset dataset, String title, String xlabel, String ylabel,
                                          ArrayList<String> comparator) {

        JFreeChart chart = ChartFactory.createBarChart(
                title, xlabel,
                ylabel, dataset);
        chart.setBackgroundPaint(null);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(null);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }

}