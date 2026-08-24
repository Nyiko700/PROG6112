/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.hospitalsystem;

/**
 *
 * @author DELL
 */
import java.util.*;
import java.util.stream.Collectors;

// ==================== PATIENT CATEGORY ====================

enum PatientCategory {
    INPATIENT,
    OUTPATIENT,
    EMERGENCY
}

// ==================== PATIENT ====================

class Patient {

    private String patientId;
    private String firstName;
    private String surname;
    private int age;
    private PatientCategory category;

    public Patient(String patientId, String firstName, String surname, int age, PatientCategory category) {

        this.patientId = patientId;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.category = category;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public PatientCategory getCategory() {
        return category;
    }

    public void setCategory(PatientCategory category) {
        this.category = category;
    }

    public void displayDetails() {
        System.out.println("ID: " + patientId + " | Name: " + firstName + " " + surname + " | Age: " + age + " | Category: " + category);
    }
}

// ==================== INPATIENT ====================

class Inpatient extends Patient {

    private int wardNumber;
    private String bedNumber;

    public Inpatient(String patientId, String firstName, String surname, int age, int wardNumber, String bedNumber) {

        super(patientId, firstName, surname, age, PatientCategory.INPATIENT);

        this.wardNumber = wardNumber;
        this.bedNumber = bedNumber;
    }

    public Inpatient(String patientId, String firstName, String surname, int age, PatientCategory category, int wardNumber, String bedNumber) {

        super(patientId, firstName, surname, age, category);

        this.wardNumber = wardNumber;
        this.bedNumber = bedNumber;
    }

    public int getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    @Override
    public void displayDetails() {

        super.displayDetails();

        System.out.println( " -> Ward: " + wardNumber +  " | Bed: " + (bedNumber == null ? "Not Allocated" : bedNumber)
        );
    }
}

// ==================== WARD ====================

class Ward {

    private static final int ROWS = 4;
    private static final int COLS = 5;

    private String[][] beds = new String[ROWS][COLS];

    private Map<String, String> bedToPatientMap = new HashMap<>();

    public Ward() {

        int count = 1;

        for (int i = 0; i < ROWS; i++) {

            for (int j = 0; j < COLS; j++) {

                beds[i][j] = String.format("B%02d", count++);
            }
        }
    }

    public boolean isBedAvailable(String bedId) {

        return !bedToPatientMap.containsKey( bedId.toUpperCase());
    }

    public boolean allocateBed(String bedId, String patientId) {

        bedId = bedId.toUpperCase();

        if (!isValidBed(bedId)) {
            return false;
        }

        if (!isBedAvailable(bedId)) {
            return false;
        }

        bedToPatientMap.put(bedId, patientId);

        return true;
    }

    public boolean releaseBed(String bedId) {

        bedId = bedId.toUpperCase();

        if (bedToPatientMap.containsKey(bedId)) {

            bedToPatientMap.remove(bedId);

            return true;
        }

        return false;
    }

    public boolean isValidBed(String bedId) {

        for (int i = 0; i < ROWS; i++) {

            for (int j = 0; j < COLS; j++) {

                if (beds[i][j].equalsIgnoreCase(bedId)) {

                    return true;
                }
            }
        }

        return false;
    }

    public void displayCompleteLayout() {

        System.out.println("\n--- Complete Ward Layout (4x5) ---");

        for (int i = 0; i < ROWS; i++) {

            for (int j = 0; j < COLS; j++) {

                String bedId = beds[i][j];

                String status = bedToPatientMap .containsKey(bedId) ? "[X]" : "[ ]";

                System.out.print(bedId + status + " ");
            }

            System.out.println();
        }

        System.out.println("[X]=Occupied, [ ]=Available");
    }

    public List<String> getAvailableBeds() {

        List<String> list = new ArrayList<>();

        for (int i = 0; i < ROWS; i++) {

            for (int j = 0; j < COLS; j++) {

                if (isBedAvailable(beds[i][j])) {

                    list.add(beds[i][j]);
                }
            }
        }

        return list;
    }

    public List<String> getOccupiedBeds() {

        return new ArrayList<>(bedToPatientMap.keySet()
        );
    }

    public Map<String, String> getBedAllocations() {

        return bedToPatientMap;
    }

    public int getTotalBeds() {

        return ROWS * COLS;
    }

    public int getOccupiedCount() {

        return bedToPatientMap.size();
    }

    public double getOccupancyPercentage() {

        return (getOccupiedCount() * 100.0)
                / getTotalBeds();
    }

    public String getBedForPatient(String patientId) {

        for (Map.Entry<String, String> entry : bedToPatientMap.entrySet()) {

            if (entry.getValue().equals(patientId)) {

                return entry.getKey();
            }
        }

        return null;
    }
}

// ==================== HOSPITAL SYSTEM ====================

class HospitalSystem {

    private List<Patient> patients = new ArrayList<>();

    private Ward ward = new Ward();

    public boolean registerPatient(Patient patient) {

        if (findPatientById(patient.getPatientId()) != null) {

            return false;
        }

        patients.add(patient);

        return true;
    }

    public Patient findPatientById(String id) {

        for (Patient p : patients) {

            if (p.getPatientId().equalsIgnoreCase(id)) {

                return p;
            }
        }

        return null;
    }

    public boolean updatePatient(String id, String newFirstName, String newSurname, int newAge, PatientCategory newCategory) {

        Patient p = findPatientById(id);

        if (p == null) {
            return false;
        }

        p.setFirstName(newFirstName);
        p.setSurname(newSurname);
        p.setAge(newAge);
        p.setCategory(newCategory);

        return true;
    }

    public boolean deletePatient(String id) {

        Patient p = findPatientById(id);

        if (p == null) {
            return false;
        }

        String bed = ward.getBedForPatient(id);

        if (bed != null) {

            ward.releaseBed(bed);
        }

        if (p instanceof Inpatient) {

            ((Inpatient) p).setBedNumber(null);
        }

        patients.remove(p);

        return true;
    }

    public List<Patient> getAllPatients() {

        return patients;
    }

    public boolean allocateBedToInpatient( String patientId, String bedId) {

        Patient p = findPatientById(patientId);

        if (p == null) {
            return false;
        }

        if (p.getCategory() != PatientCategory.INPATIENT && !(p instanceof Inpatient)) {

            return false;
        }

        if (ward.getOccupiedCount() >= ward.getTotalBeds()) {

            return false;
        }

        if (!ward.isValidBed(bedId)) {
            return false;
        }

        String existingBed = ward.getBedForPatient(patientId);

        if (existingBed != null) {

            ward.releaseBed(existingBed);
        }

        if (!ward.isBedAvailable(bedId)) {
            return false;
        }

        boolean success = ward.allocateBed(bedId, patientId);

        if (success && p instanceof Inpatient) {

            Inpatient inpatient = (Inpatient) p;

            inpatient.setBedNumber(bedId.toUpperCase());

            inpatient.setWardNumber(1);
        }

        return success;
    }

    public boolean releaseBed(String bedId) {

        String patientId = ward.getBedAllocations().get(bedId.toUpperCase());

        boolean released = ward.releaseBed(bedId);

        if (released && patientId != null) {

            Patient patient = findPatientById(patientId);

            if (patient instanceof Inpatient) {

                ((Inpatient) patient).setBedNumber(null);
            }
        }

        return released;
    }

    public Ward getWard() {

        return ward;
    }

    public void displayAllPatients() {

        if (patients.isEmpty()) {

            System.out.println("No patients registered.");

            return;
        }

        for (Patient p : patients) {

            p.displayDetails();
        }
    }

    public void reportOccupancy() {

        System.out.println("Total Registered Patients: " + patients.size()
        );

        System.out.println("Total Occupied Beds: " + ward.getOccupiedCount()
        );

        System.out.println("Total Available Beds: " + ward.getAvailableBeds().size()
        );

        System.out.printf("Ward Occupancy: %.2f%%%n", ward.getOccupancyPercentage());
    }

    public List<Patient> getPatientsSortedBySurname() {

        return patients.stream().sorted(Comparator.comparing(Patient::getSurname)).collect(Collectors.toList());
    }

    public List<Patient> getPatientsSortedById() {

        return patients.stream().sorted(Comparator.comparing(Patient::getPatientId)).collect(Collectors.toList());
    }
}

// ==================== MAIN ====================

class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static HospitalSystem system = new HospitalSystem();

    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            printMenu();

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    registerPatientMenu();
                    break;

                case "2":
                    searchPatientMenu();
                    break;

                case "3":
                    updatePatientMenu();
                    break;

                case "4":
                    deletePatientMenu();
                    break;

                case "5":
                    system.displayAllPatients();
                    break;

                case "6":
                    allocateBedMenu();
                    break;

                case "7":
                    releaseBedMenu();
                    break;

                case "8":
                    system.getWard().displayCompleteLayout();
                    break;

                case "9":

                    System.out.println("Available: "  + system.getWard().getAvailableBeds());

                    break;

                case "10":

                    System.out.println("Occupied: " + system.getWard().getBedAllocations()
                    );

                    break;

                case "11":
                    reportsMenu();
                    break;

                case "12":
                    sortedDisplayMenu();
                    break;

                case "0":

                    running = false;

                    System.out.println("Exiting system.");

                    break;

                default:

                    System.out.println("Invalid choice." );
            }
        }
    }

    private static void printMenu() {

        System.out.println("\n===== HOSPITAL PATIENT ADMISSION SYSTEM =====");

        System.out.println("1. Register New Patient");

        System.out.println("2. Search Patient by ID");

        System.out.println("3. Update Patient Details");

        System.out.println("4. Delete Patient");

        System.out.println("5. Display All Patients");

        System.out.println("6. Allocate Bed to Inpatient");

        System.out.println("7. Release Bed");

        System.out.println("8. Display Complete Ward Layout");

        System.out.println("9. Display Available Beds");

        System.out.println("10. Display Occupied Beds");

        System.out.println("11. Reports (Totals & Occupancy %)");

        System.out.println("12. Sort Patients (Surname / ID)");

        System.out.println("0. Exit");

        System.out.print("Enter choice: ");
    }

    private static void registerPatientMenu() {

        try {

            System.out.print("Patient ID: ");

            String id = scanner.nextLine();

            System.out.print("First Name: ");

            String f = scanner.nextLine();

            System.out.print("Surname: ");

            String s = scanner.nextLine();

            System.out.print("Age: ");

            int age = Integer.parseInt(scanner.nextLine());

            System.out.print("Category " + "(INPATIENT/OUTPATIENT/EMERGENCY): ");

            PatientCategory cat = PatientCategory.valueOf(scanner.nextLine().toUpperCase());

            Patient p;

            if (cat == PatientCategory.INPATIENT) {

                p = new Inpatient(id, f, s, age, 1, null);

            } else {

                p = new Patient(id, f, s, age, cat);
            }

            if (system.registerPatient(p)) {

                System.out.println("Patient registered successfully.");

            } else {

                System.out.println("Error: Duplicate Patient ID!");
            }

        } catch (
                NumberFormatException e) {

            System.out.println("Error: Age must be a number.");

        } catch (
                IllegalArgumentException e) {

            System.out.println("Error: Invalid patient category.");
        }
    }

    private static void searchPatientMenu() {

        System.out.print("Enter Patient ID: ");

        String id = scanner.nextLine();

        Patient p = system.findPatientById(id);

        if (p != null) {

            p.displayDetails();

        } else {

            System.out.println("Patient not found.");
        }
    }

    private static void updatePatientMenu() {

        try {

            System.out.print("Enter Patient ID to update: ");

            String id = scanner.nextLine();

            Patient existing = system.findPatientById(id);

            if (existing == null) {

                System.out.println("Not found.");

                return;
            }

            System.out.print("New First Name: ");

            String f = scanner.nextLine();

            System.out.print("New Surname: ");

            String s = scanner.nextLine();

            System.out.print("New Age: ");

            int age = Integer.parseInt(scanner.nextLine());

            System.out.print("New Category: ");

            PatientCategory cat = PatientCategory.valueOf(scanner.nextLine().toUpperCase());

            if (system.updatePatient(id, f, s, age, cat)) {

                System.out.println("Updated.");

            } else {

                System.out.println("Update failed.");
            }

        } catch (
                NumberFormatException e) {

            System.out.println("Error: Age must be a number.");

        } catch (IllegalArgumentException e) {

            System.out.println("Error: Invalid category.");
        }
    }

    private static void deletePatientMenu() {

        System.out.print("Enter Patient ID to delete: ");

        String id = scanner.nextLine();

        if (system.deletePatient(id)) {

            System.out.println("Deleted and bed released."
            );

        } else {

            System.out.println("Patient not found."
            );
        }
    }

    private static void allocateBedMenu() {

        system.getWard().displayCompleteLayout();

        System.out.print("Enter Inpatient ID: ");

        String pid = scanner.nextLine();

        System.out.print("Enter Bed ID (e.g., B01): ");

        String bed = scanner.nextLine();

        if (system.allocateBedToInpatient(pid, bed)) {

            System.out.println("Bed " + bed.toUpperCase() + " allocated to " + pid);

        } else {

            System.out.println("Failed - check patient, " + "bed availability, " + "and ward capacity." );
        }
    }

    private static void releaseBedMenu() {

        System.out.print("Enter Bed ID to release: ");

        String bed = scanner.nextLine();

        if (system.releaseBed(bed)) {

            System.out.println("Bed released.");

        } else {

            System.out.println("Bed not occupied or invalid.");
        }
    }

    private static void reportsMenu() {

        System.out.println( "\n--- REPORTS ---");

        system.displayAllPatients();

        System.out.println("\nAvailable Beds: " + system.getWard().getAvailableBeds());

        System.out.println("Occupied Beds: " + system.getWard().getBedAllocations());

        system.reportOccupancy();
    }

    private static void sortedDisplayMenu() {

        System.out.println("1. Sort by Surname");

        System.out.println("2. Sort by Patient ID");

        System.out.print("Enter choice: ");

        String c = scanner.nextLine();

        if (c.equals("1")) {

            system.getPatientsSortedBySurname().forEach(Patient::displayDetails);

        } else if (c.equals("2")) {

            system.getPatientsSortedById().forEach(Patient::displayDetails);

        } else {

            System.out.println("Invalid choice.");
        }
    }
}