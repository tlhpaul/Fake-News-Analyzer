import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PolarAxisLocation;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Creates a scatter plot, fills data and populates the chart
 *
 */
public class ScatterPlotChart extends JFrame {

	private static final long serialVersionUID = 1L;

	static {
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",
                false));
    }

    /**
     * Creates a static scatter plot
     * 
     * @param title is the title of the scatter plot
     * @param compareList is the list of entities to be comapred
     * @param xLabel is the x axis label
     * @param yLabel is the y axis label
     * @param values are the list of lists of xy pairs to be used as data points
     */
    public ScatterPlotChart(String title, ArrayList<String> compareList, String xLabel, String yLabel,
                            ArrayList<ArrayList<Integer[]>> values) {
        super(title);
        JFreeChart chart = ChartFactory.createScatterPlot(
                title,
                xLabel, // x axis label
                yLabel, // y axis label
                createDataset(compareList, values),
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
        );
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(550, 550));
        panel.setMouseZoomable(false);
        this.add(panel);
    }


    /**
     * Creates the dataset for the scatter plot
     *
     * @param compareList is the list of compared groups
     * @param valueList is the list of lists of xy pairs of values to be added
     * @return the XYSeriesCollection containing all XY pairs for the scatter plot
     */
    private static XYSeriesCollection createDataset(ArrayList<String> compareList,
                                                    ArrayList<ArrayList<Integer[]>> valueList) {

        XYSeriesCollection result = new XYSeriesCollection();
        for(int i = 0; i < valueList.size(); i++) {

            String comparators = compareList.get(i);
            ArrayList<Integer[]> values = valueList.get(i);

            XYSeries series = new XYSeries(comparators);
            for (int j = 0; j < values.size(); j++) {
                double x = values.get(j)[0];
                double y = values.get(j)[1];
                series.add(x, y);
            }
            result.addSeries(series);
        }
        return result;
    }

    /**
     * Creates the chart itself
     *
     * @param dataset is the set of data being uploaded into the chart
     * @param title is the title of the chart
     * @return the formed chart
     */
    private static JFreeChart createChart(XYSeriesCollection dataset, String title) {
        ValueAxis radiusAxis = new NumberAxis();
        radiusAxis.setTickLabelsVisible(false);
        DefaultPolarItemRenderer renderer = new DefaultPolarItemRenderer();
        renderer.setShapesVisible(false);
        PolarPlot plot = new PolarPlot(dataset, radiusAxis, renderer);
        plot.setCounterClockwise(true);
        plot.setAxisLocation(PolarAxisLocation.EAST_BELOW);
        plot.setAngleOffset(0);
        plot.setBackgroundPaint(new Color(0x00f0f0f0));
        plot.setRadiusGridlinePaint(Color.blue);
        JFreeChart chart = new JFreeChart(
                title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.setBackgroundPaint(Color.white);
        return chart;
    }


}

