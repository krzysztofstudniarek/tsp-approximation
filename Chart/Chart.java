/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Chart;

/**
 *
 * @author Krzysiek
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
//import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class Chart {

    XYSeries bests;
    String name;

    /**
     * @param ChartName	Name of chart
     */
    public Chart(String ChartName) {
        this.bests = new XYSeries("Best permutations strength");
        this.name = ChartName;

    }

    /**
     * Adding of one tuple
     * 
     * @param num 				tuple key
     * @param bestStrength		best strength of the tuple
     * @param medianStrength	median strength of the tuple
     */
    public void addTuple(int num,int bestStrength, int medianStrength) {
        bests.add(num, bestStrength);
    }

    
    /**
     * Creation of dataset for displaying the graph
     * 
     * @return dataset of graph
     */
    private XYSeriesCollection createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(bests);
        return dataset;
    }


    /**
     *  Displays the graph
     */
    public void displayChart() {

        ApplicationFrame frame = new ApplicationFrame(this.name);

        JFreeChart chart = ChartFactory.createXYLineChart(
                this.name,
                "Number of generations",
                "Total distance",
                createDataset(),
                PlotOrientation.VERTICAL,
                true,
                false,
                false
         );
        
        //NumberAxis xax = new NumberAxis("x");
        //NumberAxis yax = new NumberAxis("y");
        XYSplineRenderer a = new XYSplineRenderer();
        a.setPrecision(10);
        a.setSeriesLinesVisible(0, true);
        a.setSeriesShapesVisible(0, false);
        a.setSeriesLinesVisible(1, true);
        a.setSeriesShapesVisible(1, false); 
        //XYPlot xyplot = new XYPlot(createDataset(), xax, yax, a);

        //JFreeChart chart = new JFreeChart(xyplot);

        ChartPanel chartPanel = new ChartPanel(chart);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);

    }
}
