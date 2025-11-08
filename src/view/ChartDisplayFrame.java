package view;

import javax.swing.*;
import java.awt.*;

/**
 * A dedicated JFrame to display the JFreeChart visualization.
 */
public class ChartDisplayFrame extends JFrame {

    public ChartDisplayFrame() {
        super("Classification Performance Chart");

        // Use the SimpleChartPanel component (which contains the JFreeChart)
        SimpleChartPanel chartPanel = new SimpleChartPanel();

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