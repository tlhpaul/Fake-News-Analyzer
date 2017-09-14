import java.awt.*;
import java.util.ArrayList;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.ui.ApplicationFrame;

public class MultiLineChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	static {
        // set a theme using the new shadow generator
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",
                true));
    }

    private ArrayList<Color> makeAllColors(){
        ArrayList<Color> allColors = new ArrayList<>();
        allColors.add(Color.RED);
        allColors.add(Color.BLACK);
        allColors.add(Color.BLUE);
        allColors.add(Color.GREEN);
        allColors.add(Color.MAGENTA);
        allColors.add(Color.ORANGE);
        allColors.add(Color.YELLOW);
        allColors.add(Color.GRAY);
        allColors.add(Color.PINK);
        allColors.add(Color.CYAN);
        allColors.add(Color.DARK_GRAY);
        allColors.add(Color.LIGHT_GRAY);
        return allColors;
    }

    /**
     * Creates a new  instance.
     *
     * @param title the frame title.
     */
    public MultiLineChart(String title, String xLabel, String yLabel, ArrayList<ArrayList<ArrayList<Integer>>> values,
                          ArrayList<String> titles) {

        super(title);
        JFreeChart xylineChart = createChart(
                createDataset(values, titles),
                title ,
                xLabel,
                yLabel
                );

        ArrayList<Color> colorList = makeAllColors();
        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );

        for(int i = 0; i < titles.size(); i++){
            renderer.setSeriesPaint( i , colorList.get(i));
            renderer.setSeriesStroke(i, new BasicStroke(3.0f));
        }

        plot.setRenderer( renderer );
        setContentPane( chartPanel );
    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private static XYSeriesCollection createDataset(ArrayList<ArrayList<ArrayList<Integer>>> values,
                                                    ArrayList<String> titles) {

        final XYSeriesCollection dataset = new XYSeriesCollection();

        for(int i = 0; i < values.size(); i++){
            dataset.addSeries(new XYSeries(titles.get(i)));
            XYSeries currentSeries = dataset.getSeries(i);
            for(int j = 0; j < values.get(i).size(); j++){
                int x = j;
                for (int k = 0; k < values.get(i).get(j).size(); k ++) {
                	int y = values.get(i).get(j).get(k);
                	currentSeries.add(x, y);
                }
                
            }
        }

        return dataset;
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private static JFreeChart createChart(XYSeriesCollection dataset, String title, String xLabel, String yLabel) {
         JFreeChart multiLine = ChartFactory.createXYLineChart(
                title ,
                xLabel ,
                yLabel,
                dataset ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        return multiLine;
    }
}

