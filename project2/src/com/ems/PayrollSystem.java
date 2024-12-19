/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ems;

/**
 *
 * @author abdo
 */
public class PayrollSystem {
     private static PayrollSystem instance; // Singleton instance

    private PayrollSystem() {
        // Private constructor to prevent direct instantiation
    }

    public static PayrollSystem getInstance() {
        if (instance == null) {
            instance = new PayrollSystem();
        }
        return instance;
    }

    public void processPayroll(Employee employee) {
        System.out.println("Processing payroll for: " + employee.getName());
        System.out.println("Salary: " + employee.getSalary());
        // Additional logic like bonuses, deductions, etc., can be added here
    }
}
