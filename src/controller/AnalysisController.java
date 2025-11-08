package controller;

import model.PatientRecord;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages the execution of data mining algorithms (Weka API integration).
 */
public class AnalysisController {

    public String runJ48Classification(List<PatientRecord> records) throws Exception {
        if (records.isEmpty()) {
            return "Error: No data records loaded.";
        }

        // 1. Convert Java List into Weka's required Instances structure
        Instances data = convertToWekaInstances(records, "MedicalData");

        // 2. Instantiate the J48 Classifier
        J48 j48Classifier = new J48();
        // j48Classifier.buildClassifier(data); // <-- REMOVED (Not needed for cross-validation)

        // 3. Evaluation setup and 10-fold cross-validation
        Evaluation evaluation = new Evaluation(data); // Initialize evaluation

        // Perform 10-fold cross-validation for robust evaluation
        Random rand = new Random(1); // Using a seed (1) for reproducible results
        evaluation.crossValidateModel(j48Classifier, data, 10, rand);

        // 4. Return the comprehensive summary string for display
        // <-- CORRECTED: Removed the invalid '[7]' from the end of the line
        return evaluation.toSummaryString("\n--- J48 Classification Results (10-Fold Cross-Validation) ---\n", false);
    }

    // Helper method: Maps PatientRecord attributes to Weka Attributes
    private Instances convertToWekaInstances(List<PatientRecord> records, String relationName) {
        ArrayList<Attribute> attributes = new ArrayList<>();

        // Define Numeric Attributes (Age, BMI)
        attributes.add(new Attribute("Age"));
        attributes.add(new Attribute("BMI"));

        // Define Diagnosis (Class Attribute) as Nominal (must list all possible values)
        List<String> nominalValues = new ArrayList<>();
        nominalValues.add("LowRisk");
        nominalValues.add("HighRisk");
        attributes.add(new Attribute("Diagnosis", nominalValues));

        // Create Weka Instances collection
        Instances data = new Instances(relationName, attributes, records.size());
        data.setClassIndex(data.numAttributes() - 1); // Set Diagnosis as the class

        // Populate the Instances object with data from the PatientRecords
        for (PatientRecord record : records) {
            double[] vals = new double[data.numAttributes()];

            // Populate values for Age and BMI
            // vals = record.getAge(); // <-- This was the error
            vals[0] = record.getAge(); // <-- CORRECTED
            vals[1] = record.getBmi();

            // Populate value for Diagnosis (mapping the string to Weka's internal index)
            // Assumes getDiagnosis() returns either "LowRisk" or "HighRisk"
            int classIndex = data.attribute(2).indexOfValue(record.getDiagnosis());
            vals[2] = classIndex;

            data.add(new DenseInstance(1.0, vals));
        }
        return data;
    }
}