package com.ems;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EmployeeManagementGUI extends JFrame {

    // Components for Employee Tab
    private JTextField empNameField, empTypeField, empSalaryField, empDeptIdField;
    private JTextArea empOutputArea;
    private JButton addEmpButton, viewEmpButton;

    // Components for Department Tab
    private JTextField deptNameField;
    private JTextArea deptOutputArea;
    private JButton addDeptButton, viewDeptButton;
    private JTextArea notificationArea;

    public EmployeeManagementGUI() {
        // Inside EmployeeManagementGUI class


        setTitle("Employee and Department Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create Tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Employee Tab
        JPanel employeePanel = createEmployeePanel();
        tabbedPane.addTab("Employee Management", employeePanel);

        // Department Tab
        JPanel departmentPanel = createDepartmentPanel();
        tabbedPane.addTab("Department Management", departmentPanel);

        // Add tabs to frame
        add(tabbedPane);
    }

    private JPanel createEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        inputPanel.add(new JLabel("Name:"));
        empNameField = new JTextField();
        inputPanel.add(empNameField);

        inputPanel.add(new JLabel("Type:"));
        empTypeField = new JTextField();
        inputPanel.add(empTypeField);

        inputPanel.add(new JLabel("Salary:"));
        empSalaryField = new JTextField();
        inputPanel.add(empSalaryField);

        inputPanel.add(new JLabel("Department ID:"));
        empDeptIdField = new JTextField();
        inputPanel.add(empDeptIdField);

        addEmpButton = new JButton("Add Employee");
        viewEmpButton = new JButton("View Employees");

        inputPanel.add(addEmpButton);
        inputPanel.add(viewEmpButton);
        

        // Output Area
        empOutputArea = new JTextArea();
        empOutputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(empOutputArea);

        // Add components to panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Adding notification area to the GUI

        // Add Button Listeners
        addEmpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        viewEmpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewEmployees();
            }
        });

        return panel;
    }

    private JPanel createDepartmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Department Details"));

        inputPanel.add(new JLabel("Department Name:"));
        deptNameField = new JTextField();
        inputPanel.add(deptNameField);

        addDeptButton = new JButton("Add Department");
        viewDeptButton = new JButton("View Departments");

        inputPanel.add(addDeptButton);
        inputPanel.add(viewDeptButton);

        // Output Area
        deptOutputArea = new JTextArea();
        deptOutputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(deptOutputArea);

        // Add components to panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add Button Listeners
        addDeptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDepartment();
            }
        });

        viewDeptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewDepartments();
            }
        });

        return panel;
    }

    private void addEmployee() {
        String name = empNameField.getText();
        String type = empTypeField.getText();
        double salary = Double.parseDouble(empSalaryField.getText());
        int departmentId = Integer.parseInt(empDeptIdField.getText());

        String sql = "INSERT INTO employees (name, type, salary, department_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, type);
            stmt.setDouble(3, salary);
            stmt.setInt(4, departmentId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                empOutputArea.append("Employee added: " + name + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            empOutputArea.append("Error adding employee.\n");
        }
    }

    private void viewEmployees() {
        String sql = "SELECT * FROM employees";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            empOutputArea.setText(""); // Clear output
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                double salary = rs.getDouble("salary");
                int deptId = rs.getInt("department_id");

                empOutputArea.append("ID: " + id + ", Name: " + name + ", Type: " + type
                        + ", Salary: " + salary + ", Dept ID: " + deptId + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            empOutputArea.append("Error retrieving employees.\n");
        }
    }

    private void addDepartment() {
        String departmentName = deptNameField.getText();

        if (departmentName.isEmpty()) {
            deptOutputArea.append("Error: Department name cannot be empty.\n");
            return;
        }

        String sql = "INSERT INTO departments (name) VALUES (?);";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, departmentName);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                deptOutputArea.append("Department added: " + departmentName + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            deptOutputArea.append("Error adding department.\n");
        }
    }

    private void viewDepartments() {
        String sql = "SELECT * FROM departments;";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            deptOutputArea.setText(""); // Clear output
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                deptOutputArea.append("ID: " + id + ", Name: " + name + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            deptOutputArea.append("Error retrieving departments.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeManagementGUI gui = new EmployeeManagementGUI();
            gui.setVisible(true);
        });
    }
}
