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

    // The method is declared to return an Evaluation object.
    public Evaluation runJ48Classification(List<PatientRecord> records) throws Exception {

        // FIX 1: If data is empty, throw an Exception instead of returning a String.
        if (records.isEmpty()) {
            throw new Exception("Error: No data records loaded.");
        }

        // 1. Convert Java List into Weka's required Instances structure
        Instances data = convertToWekaInstances(records, "MedicalData");

        // 2. Instantiate the J48 Classifier
        J48 j48Classifier = new J48();
        // NOTE: While crossValidateModel does internal building, including this ensures Weka is initialized correctly
        // and is standard practice.
        j48Classifier.buildClassifier(data); // Train the J48 classifier [3]

        // 3. Evaluation setup and 10-fold cross-validation
        Evaluation evaluation = new Evaluation(data); // Initialize evaluation [4]

        // Perform 10-fold cross-validation for robust evaluation [4]
        Random rand = new Random(1); // Using a seed (1) for reproducible results
        evaluation.crossValidateModel(j48Classifier, data, 10, rand);

        // 4. Print results to console (optional but helpful)
        System.out.println(evaluation.toSummaryString("\n--- J48 Classification Results (10-Fold Cross-Validation) ---\n", false));

        return evaluation; // Return the full evaluation object
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
        data.setClassIndex(data.numAttributes() - 1); // Set Diagnosis as the class [3]

        // Populate the Instances object with data from the PatientRecords
        for (PatientRecord record : records) {
            double[] vals = new double[data.numAttributes()];

            // Populate values for Age and BMI
            // Index 0: Age (getAge() now returns double)
            vals[1] = record.getAge();
            // Index 1: BMI (double)
            vals[1] = record.getBmi();

            // Populate value for Diagnosis (mapping the string to Weka's internal index)
            int classIndex = data.attribute(2).indexOfValue(record.getDiagnosis());
            // Index 2: Diagnosis (must be cast to double for the double array)
            vals[2] = (double) classIndex;

            // Create a dense instance and add it to the Weka data structure
            data.add(new DenseInstance(1.0, vals));
        }
        return data;
    }
}