package model;

/**
 * Defines the domain object for a single patient record (Collections Framework).
 */
public class PatientRecord {
    private final String patientID;
    private final int age; // Stored as int
    private final double bmi;
    private final String diagnosis; // The target class attribute

    public PatientRecord(String patientID, int age, double bmi, String diagnosis) {
        this.patientID = patientID;
        this.age = age;
        this.bmi = bmi;
        this.diagnosis = diagnosis;
    }

    // Getters for the Controller to access the data
    public String getPatientID() { return patientID; }

    // FIX: Corrected the syntax to simply return the 'age' field.
    // Java automatically casts the int 'age' to a double for the return type.
    public double getAge() { return age; }

    public double getBmi() { return bmi; }
    public String getDiagnosis() { return diagnosis; }
}