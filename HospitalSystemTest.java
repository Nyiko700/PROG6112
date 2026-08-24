/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.hospitalsystem;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author DELL
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class HospitalSystemTest {
    private HospitalSystem hospital;

    @BeforeEach
    public void setUp() {
        hospital = new HospitalSystem();
    }

    @Test
    public void testRegisterPatient() {
        Patient p = new Patient("P001", "John", "Doe", 30, PatientCategory.OUTPATIENT);
        assertTrue(hospital.registerPatient(p));
        assertEquals(1, hospital.getAllPatients().size());
    }

    @Test
    public void testSearchPatient() {
        Patient p = new Patient("P001", "John", "Doe", 30, PatientCategory.OUTPATIENT);
        hospital.registerPatient(p);
        assertNotNull(hospital.findPatientById("P001"));
        assertNull(hospital.findPatientById("P999"));
    }

    @Test
    public void testUpdatePatient() {
        Patient p = new Patient("P001", "John", "Doe", 30, PatientCategory.OUTPATIENT);
        hospital.registerPatient(p);
        boolean updated = hospital.updatePatient("P001", "Jane", "Smith", 28, PatientCategory.EMERGENCY);
        assertTrue(updated);
        Patient found = hospital.findPatientById("P001");
        assertEquals("Jane", found.getFirstName());
        assertEquals("Smith", found.getSurname());
    }

    @Test
    public void testDeletePatient() {
        Patient p = new Patient("P001", "John", "Doe", 30, PatientCategory.OUTPATIENT);
        hospital.registerPatient(p);
        assertTrue(hospital.deletePatient("P001"));
        assertEquals(0, hospital.getAllPatients().size());
    }

    @Test
    public void testAllocateBed() {
        Inpatient ip = new Inpatient("I001", "Alice", "Brown", 40, 1, null);
        hospital.registerPatient(ip);
        assertTrue(hospital.allocateBedToInpatient("I001", "B01"));
        assertFalse(hospital.getWard().isBedAvailable("B01"));
    }

    @Test
    public void testReleaseBed() {
        Inpatient ip = new Inpatient("I001", "Alice", "Brown", 40, 1, null);
        hospital.registerPatient(ip);
        hospital.allocateBedToInpatient("I001", "B01");
        assertTrue(hospital.releaseBed("B01"));
        assertTrue(hospital.getWard().isBedAvailable("B01"));
    }

    @Test
    public void testPreventDuplicatePatientId() {
        Patient p1 = new Patient("P001", "John", "Doe", 30, PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P001", "Jane", "Smith", 25, PatientCategory.OUTPATIENT);
        assertTrue(hospital.registerPatient(p1));
        assertFalse(hospital.registerPatient(p2));
    }

    @Test
    public void testPreventAllocatingOccupiedBed() {
        Inpatient ip1 = new Inpatient("I001", "Alice", "Brown", 40, 1, null);
        Inpatient ip2 = new Inpatient("I002", "Bob", "White", 35, 1, null);
        hospital.registerPatient(ip1);
        hospital.registerPatient(ip2);
        assertTrue(hospital.allocateBedToInpatient("I001", "B01"));
        assertFalse(hospital.allocateBedToInpatient("I002", "B01"));
    }

    @Test
    public void testPreventAllocationWhenAllBedsOccupied() {
        // Fill all 20 beds
        for (int i = 1; i <= 20; i++) {
            String pid = String.format("I%03d", i);
            Inpatient ip = new Inpatient(pid, "Name" + i, "Surname" + i, 20 + i, 1, null);
            hospital.registerPatient(ip);
            hospital.allocateBedToInpatient(pid, String.format("B%02d", i));
        }
        Inpatient extra = new Inpatient("I021", "Extra", "Patient", 50, 1, null);
        hospital.registerPatient(extra);
        assertFalse(hospital.allocateBedToInpatient("I021", "B01"));
        assertEquals(20, hospital.getWard().getOccupiedCount());
    }

    @Test
    public void testSortBySurname() {
        hospital.registerPatient(new Patient("P003", "John", "Zebra", 30, PatientCategory.OUTPATIENT));
        hospital.registerPatient(new Patient("P001", "Alice", "Apple", 25, PatientCategory.OUTPATIENT));
        hospital.registerPatient(new Patient("P002", "Bob", "Mango", 28, PatientCategory.OUTPATIENT));
        List<Patient> sorted = hospital.getPatientsSortedBySurname();
        assertEquals("Apple", sorted.get(0).getSurname());
        assertEquals("Mango", sorted.get(1).getSurname());
        assertEquals("Zebra", sorted.get(2).getSurname());
    }

    @Test
    public void testSortByPatientId() {
        hospital.registerPatient(new Patient("P003", "John", "Zebra", 30, PatientCategory.OUTPATIENT));
        hospital.registerPatient(new Patient("P001", "Alice", "Apple", 25, PatientCategory.OUTPATIENT));
        List<Patient> sorted = hospital.getPatientsSortedById();
        assertEquals("P001", sorted.get(0).getPatientId());
        assertEquals("P003", sorted.get(1).getPatientId());
    }

    @Test
    public void testOutpatientCannotGetBed() {
        Patient op = new Patient("O001", "Out", "Patient", 30, PatientCategory.OUTPATIENT);
        hospital.registerPatient(op);
        assertFalse(hospital.allocateBedToInpatient("O001", "B01"));
    }
}
