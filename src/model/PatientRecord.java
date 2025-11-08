package model;

/**
 * Defines the domain object for a single patient record.
 * This object is stored in the Java Collections Framework (List<PatientRecord>).
 */
public class PatientRecord {
    private final String patientID;
    private final double age;
    private final double bmi;
    private final String diagnosis; // The target class attribute

    public PatientRecord(String patientID, double age, double bmi, String diagnosis) {
        this.patientID = patientID;
        this.age = age;
        this.bmi = bmi;
        this.diagnosis = diagnosis;
    }

    // Getters for the Controller to access the data
    public String getPatientID() { return patientID; }
    public double getAge() { return age; }
    public double getBmi() { return bmi; }
    public String getDiagnosis() { return diagnosis; }
}