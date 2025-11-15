package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import weka.classifiers.Evaluation;

import javax.swing.*;
import java.awt.*;

/**
 * A simple panel demonstrating JFreeChart integration for visualization.[8]
 * Now displays dynamic data from the Weka Evaluation result.
 */
public class SimpleChartPanel extends JPanel {

    // MODIFICATION: Accepts the Evaluation object
    public SimpleChartPanel(Evaluation evaluation) {
        super(new BorderLayout());

        // 1. Create dataset based on Weka Evaluation metrics
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            // Get classification results from Weka's Evaluation object
            double correct = evaluation.pctCorrect();
            double incorrect = evaluation.pctIncorrect();

            // Add dynamic values to the dataset (Actual Weka results)
            dataset.addValue(correct, "Correctly Classified", "Accuracy (%)");
            dataset.addValue(incorrect, "Incorrectly Classified", "Error (%)");
        } catch (Exception e) {
            // Fallback in case of metric error
            dataset.addValue(0.0, "Data Error", "Error (%)");
        }

        // 2. Create the JFreeChart object [8]
        JFreeChart chart = ChartFactory.createBarChart(
                "J48 Classification Performance (10-Fold CV)",
                "Metric Type",
                "Percentage Value",
                dataset
        );

        // 3. Embed the chart into a ChartPanel, a Swing component [8]
        ChartPanel chartPanel = new ChartPanel(chart);
        this.add(chartPanel, BorderLayout.CENTER);
    }
}