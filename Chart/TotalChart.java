/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Chart;

/**
 *
 * @author Krzysiek
 */

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TotalChart {

    XYSeries ApproximationAlgorithm;
    XYSeries GeneticAlgorithm;
    XYSeries TwoOptAlgorithm;
    String name;

    /**
     * @param ChartName	Name of the chart
     */
    public TotalChart(String ChartName) {
        this.ApproximationAlgorithm = new XYSeries("Approximation algorithm results");
        this.GeneticAlgorithm = new XYSeries("Genetic algorithm results");
        this.TwoOptAlgorithm = new XYSeries("2-Opt algorithm results");
        this.name = ChartName;

    }

    /**
     * Adding one tuple
     * 
     * @param num 			key of the tuple
     * @param optStrength	2-Opt algorithm strength
     * @param genStrength	Genetic algorithm strength
     * @param approxStrengts	Approximation algorithm strength
     */
    public void addTuple(int num, double optStrength, double genStrength,double approxStrengts) {
        this.ApproximationAlgorithm.add(num,approxStrengts);
        this.GeneticAlgorithm.add(num, genStrength);
        this.TwoOptAlgorithm.add(num,optStrength);
    }

    /**
     * Generating data set for the graph
     * 
     * @return
     */
    private XYSeriesCollection createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(this.ApproximationAlgorithm);
        dataset.addSeries(this.GeneticAlgorithm);
        dataset.addSeries(this.TwoOptAlgorithm);
        return dataset;
    }


    /**
     *  Displaying the graph
     */
    public void displayChart() {

        JFrame frame = new JFrame(this.name);
        frame.setLayout(new BorderLayout());
        
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
       
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        

    }
}
