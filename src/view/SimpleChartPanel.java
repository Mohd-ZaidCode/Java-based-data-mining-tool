package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

/**
 * A simple panel demonstrating JFreeChart integration for visualization.
 */
public class SimpleChartPanel extends JPanel {

    public SimpleChartPanel() {
        super(new BorderLayout());

        // 1. Create a sample dataset (simulating classification accuracy results)
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(90.5, "Correct (%)", "J48 Classifier");
        dataset.addValue(9.5, "Incorrect (%)", "J48 Classifier");

        // 2. Create the JFreeChart object (using a Bar Chart for metric visualization)
        JFreeChart chart = ChartFactory.createBarChart(
                "Classification Accuracy Snapshot",
                "Result Type",
                "Percentage",
                dataset
        );

        // 3. Embed the chart into a ChartPanel, which is a Swing component
        ChartPanel chartPanel = new ChartPanel(chart);
        this.add(chartPanel, BorderLayout.CENTER);
    }
}