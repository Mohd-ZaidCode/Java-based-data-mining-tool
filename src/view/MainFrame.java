package view;

import controller.AnalysisController;
import model.DataReader;
import model.PatientRecord;
import weka.classifiers.Evaluation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

/**
 * The main application window (View layer using AWT/Swing).
 * Handles user interaction and displays results.
 */
public class MainFrame extends JFrame {
    private final AnalysisController controller = new AnalysisController();
    private final DataReader dataReader = new DataReader();
    private List<PatientRecord> loadedRecords;

    private final JTextArea resultsArea = new JTextArea(20, 50);
    private final JButton loadButton = new JButton("Load CSV Data");
    private final JButton analyzeButton = new JButton("Analysis");
    private final JLabel statusLabel = new JLabel("Status: Ready.");

    public MainFrame() {
        super("Medical Data Mining Application");
        initializeGUI();
    }

    private void initializeGUI() {
        // Set up the main layout
        setLayout(new BorderLayout(10, 10));

        // 1. Control Panel (North)
        JPanel controlPanel = new JPanel();
        controlPanel.add(loadButton);
        controlPanel.add(analyzeButton);
        add(controlPanel, BorderLayout.NORTH);

        // 2. Status Bar (South)
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(statusLabel, BorderLayout.SOUTH);

        // 3. Results Area (Center)
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        add(scrollPane, BorderLayout.CENTER);

        // Initial state
        analyzeButton.setEnabled(false);

        // Add Action Listeners
        loadButton.addActionListener(this::handleLoadAction);
        analyzeButton.addActionListener(this::handleAnalyzeAction);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Handles the "Load CSV Data" button click
    private void handleLoadAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            statusLabel.setText("Status: Loading " + selectedFile.getName() + "...");

            // Use SwingWorker for I/O to prevent the GUI from freezing
            new SwingWorker<List<PatientRecord>, Void>() {
                @Override
                protected List<PatientRecord> doInBackground() throws Exception {
                    return dataReader.loadFromCSV(selectedFile.getAbsolutePath());
                }

                @Override
                protected void done() {
                    try {
                        loadedRecords = get();
                        resultsArea.setText("Data Loaded Successfully.\nTotal Records: " + loadedRecords.size() + "\n\n");
                        analyzeButton.setEnabled(true);
                        statusLabel.setText("Status: Ready. " + loadedRecords.size() + " records loaded.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Error reading file: " + ex.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
                        statusLabel.setText("Status: Load failed.");
                        analyzeButton.setEnabled(false);
                    }
                }
            }.execute();
        }
    }

    // Handles the "Run J48 Analysis" button click
    private void handleAnalyzeAction(ActionEvent e) {
        if (loadedRecords == null || loadedRecords.isEmpty()) {
            resultsArea.setText("Please load data first.");
            return;
        }

        analyzeButton.setEnabled(false);
        loadButton.setEnabled(false);
        resultsArea.append("Starting J48 Classification... (Weka analysis running in background)\n");
        statusLabel.setText("Status: Analyzing data...");

        // SwingWorker parameterized to return the Weka Evaluation object
        new SwingWorker<Evaluation, Void>() {
            @Override
            protected Evaluation doInBackground() throws Exception {
                return controller.runJ48Classification(loadedRecords);
            }

            @Override
            protected void done() {
                analyzeButton.setEnabled(true);
                loadButton.setEnabled(true);
                try {
                    Evaluation evaluation = get();

                    // Display the text summary
                    resultsArea.append(evaluation.toSummaryString("\n--- J48 Classification Results (10-Fold Cross-Validation) ---\n", false));
                    statusLabel.setText("Status: Analysis complete.");

                    // Launch Chart with the actual Evaluation object
                    SwingUtilities.invokeLater(() -> new ChartDisplayFrame(evaluation));

                } catch (Exception ex) {
                    resultsArea.append("Analysis Failed: " + ex.getMessage() + "\n");
                    statusLabel.setText("Status: Analysis failed.");
                }
            }
        }.execute();
    }

    // Main method: Start the application on the Event Dispatch Thread (Correct Signature)
    public static void main(String []args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}