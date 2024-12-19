/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeemanager;

/**
 *
 * @author abdo
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

// Observer Interface
interface Observer {
    void update(String message);
}

// Subject Interface
interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(String message);
}

// Concrete Observer - Logs messages to console
class Logger implements Observer {
    @Override
    public void update(String message) {
        System.out.println("[LOG]: " + message);
    }
}

// Concrete Subject - Manages employees
class EmployeeManager implements Subject {
    private final List<Observer> observers = new ArrayList<>();
    private Connection connection;

    public EmployeeManager() {
        try {
            // SQL Server Database Connection
            String url = "jdbc:sqlserver://localhost:1433;databaseName=EmployeeDB;encrypt=true;trustServerCertificate=true";
            String user = "sa";
            String password = "12345";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection successful.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void addEmployee(int id, String name, String department, double baseSalary) {
        String query = "INSERT INTO Employees (EmployeeID, Name, Department, BaseSalary) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, department);
            stmt.setDouble(4, baseSalary);
            stmt.executeUpdate();
            notifyObservers("Added employee: " + name);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding employee: " + e.getMessage());
        }
    }

    public void removeEmployee(int id) {
        String query = "DELETE FROM Employees WHERE EmployeeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            notifyObservers("Removed employee with ID: " + id);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error removing employee: " + e.getMessage());
        }
    }

    public ResultSet getEmployees() throws SQLException {
        String query = "SELECT * FROM Employees";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }
}

// Main GUI Class


