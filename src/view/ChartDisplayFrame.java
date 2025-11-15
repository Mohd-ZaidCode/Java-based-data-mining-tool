package view;

import weka.classifiers.Evaluation;

import javax.swing.*;
import java.awt.*;

/**
 * A dedicated JFrame to display the JFreeChart visualization.
 */
public class ChartDisplayFrame extends JFrame {

    // MODIFICATION: Accepts the Evaluation object to pass to the panel
    public ChartDisplayFrame(Evaluation evaluation) {
        super("J48 Classification Performance Chart");

        // Pass the live evaluation data to the chart panel
        SimpleChartPanel chartPanel = new SimpleChartPanel(evaluation);

        // Add the chart panel to the center of this new frame
        this.add(chartPanel, BorderLayout.CENTER);

        // Configuration for the chart window
        this.setSize(600, 450);
        this.setLocationRelativeTo(null); // Center the window
        // Set to dispose on close so the main application stays open
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}