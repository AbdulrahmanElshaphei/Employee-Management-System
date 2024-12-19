/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeemanager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
/**
 *
 * @author abdo
 */
// Abstract Class: Template for Report Generation
abstract class EmployeeReport {
    // Template method
    public final void generateReport() {
        fetchData();
        processReportData();
        displayReport();
    }

    // Steps that can be customized by subclasses
    protected abstract void fetchData();
    protected abstract void processReportData();
    protected abstract void displayReport();
}

// Concrete Class: Department Report
class DepartmentReport extends EmployeeReport {
    private List<String> reportData = new ArrayList<>();

    @Override
    protected void fetchData() {
        // Fetch department-wise employee data
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT Department, COUNT(*) as EmployeeCount FROM Employees GROUP BY Department")) {

            reportData.clear();
            while (rs.next()) {
                String department = rs.getString("Department");
                int employeeCount = rs.getInt("EmployeeCount");
                reportData.add("Department: " + department + " - Employees: " + employeeCount);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching department data: " + e.getMessage());
        }
    }

    @Override
    protected void processReportData() {
        // Processing logic if required (e.g., sorting or filtering)
        reportData.sort(String::compareTo);
    }

    @Override
    protected void displayReport() {
        // Display the report
        System.out.println("Department Report:");
        reportData.forEach(System.out::println);
    }
}

// Concrete Class: Salary Report
class SalaryReport extends EmployeeReport {
    private List<String> reportData = new ArrayList<>();

    @Override
    protected void fetchData() {
        // Fetch salary-wise employee data
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT Name, BaseSalary FROM Employees ORDER BY BaseSalary DESC")) {

            reportData.clear();
            while (rs.next()) {
                String name = rs.getString("Name");
                double salary = rs.getDouble("BaseSalary");
                reportData.add("Employee: " + name + " - Salary: " + salary);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching salary data: " + e.getMessage());
        }
    }

    @Override
    protected void processReportData() {
        // Processing logic if required (e.g., highlighting high earners)
        // For now, no specific processing
    }

    @Override
    protected void displayReport() {
        // Display the report
        System.out.println("Salary Report:");
        reportData.forEach(System.out::println);
    }
}

// Utility Class: Database Connection
class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=EmployeeDB;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String password = "12345";
        return DriverManager.getConnection(url, user, password);
    }
}