/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeemanager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Main Class
public class EmployeeManagementSystem {
    private JFrame frame;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeManager manager;
    private BenefitCalculator benefitCalculator;

    public EmployeeManagementSystem() {
        // Initialize components
        manager = new EmployeeManager();
        Logger logger = new Logger();
        manager.attach(logger);

        benefitCalculator = new BenefitCalculator();

        // Create GUI
        frame = new JFrame("Employee Management System");
        frame.setSize(900, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        setupEmployeeTable(frame);
        setupEmployeeManagementControls(frame);
        setupBenefitCalculationControls(frame);
        setupReportGenerationControls(frame);

        frame.setVisible(true);
    }

    // Setup Employee Table
    private void setupEmployeeTable(JFrame frame) {
        JLabel tableLabel = new JLabel("Employees:");
        tableLabel.setBounds(20, 20, 100, 25);
        frame.add(tableLabel);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Department", "Base Salary"}, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);
        tableScrollPane.setBounds(20, 50, 840, 250);
        frame.add(tableScrollPane);

        refreshEmployeeTable();
    }

    // Setup Employee Management Controls
    private void setupEmployeeManagementControls(JFrame frame) {
        JButton addButton = new JButton("Add Employee");
        addButton.setBounds(20, 320, 150, 30);
        frame.add(addButton);

        JButton removeButton = new JButton("Remove Employee");
        removeButton.setBounds(190, 320, 150, 30);
        frame.add(removeButton);

        addButton.addActionListener(e -> {
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField departmentField = new JTextField();
            JTextField salaryField = new JTextField();

            Object[] inputFields = {
                "Employee ID:", idField,
                "Name:", nameField,
                "Department:", departmentField,
                "Base Salary:", salaryField
            };

            int result = JOptionPane.showConfirmDialog(frame, inputFields, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    String department = departmentField.getText();
                    double salary = Double.parseDouble(salaryField.getText());

                    manager.addEmployee(id, name, department, salary);
                    refreshEmployeeTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                manager.removeEmployee(id);
                refreshEmployeeTable();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an employee to remove.");
            }
        });
    }

    // Setup Benefit Calculation Controls
    private void setupBenefitCalculationControls(JFrame frame) {
        JLabel benefitLabel = new JLabel("Benefit Calculation:");
        benefitLabel.setBounds(20, 370, 150, 30);
        frame.add(benefitLabel);

        String[] departments = {"HR", "IT", "Finance"};
        JComboBox<String> departmentComboBox = new JComboBox<>(departments);
        departmentComboBox.setBounds(190, 370, 150, 30);
        frame.add(departmentComboBox);

        JButton calculateButton = new JButton("Calculate Benefit");
        calculateButton.setBounds(360, 370, 150, 30);
        frame.add(calculateButton);

        calculateButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                double baseSalary = Double.parseDouble(tableModel.getValueAt(selectedRow, 3).toString());
                String department = (String) departmentComboBox.getSelectedItem();

                switch (department) {
            case "HR":
                benefitCalculator.setStrategy(new HRBenefitStrategy());
                break;
            case "IT":
                benefitCalculator.setStrategy(new ITBenefitStrategy());
                break;
            case "Finance":
                benefitCalculator.setStrategy(new FinanceBenefitStrategy());
                break;
            default:
                JOptionPane.showMessageDialog(frame, "No benefit strategy for this department.");
                return;
        }

                double benefit = benefitCalculator.calculate(baseSalary);
                JOptionPane.showMessageDialog(frame, "Calculated Benefit: $" + benefit);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an employee to calculate benefits.");
            }
        });
    }

    // Setup Report Generation Controls (Template Method)
    private void setupReportGenerationControls(JFrame frame) {
        JLabel reportLabel = new JLabel("Generate Report:");
        reportLabel.setBounds(20, 420, 150, 30);
        frame.add(reportLabel);

        String[] reports = {"Department Report", "Salary Report"};
        JComboBox<String> reportComboBox = new JComboBox<>(reports);
        reportComboBox.setBounds(190, 420, 150, 30);
        frame.add(reportComboBox);

        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.setBounds(360, 420, 150, 30);
        frame.add(generateReportButton);

        generateReportButton.addActionListener(e -> {
            String selectedReport = (String) reportComboBox.getSelectedItem();
            EmployeeReport report;

            if ("Department Report".equals(selectedReport)) {
                report = new DepartmentReport();
            } else {
                report = new SalaryReport();
            }

            report.generateReport();
            JOptionPane.showMessageDialog(frame, selectedReport + " generated. Check console for details.");
        });
    }

    // Refresh Employee Table
    private void refreshEmployeeTable() {
        try (ResultSet rs = manager.getEmployees()) {
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("EmployeeID"),
                    rs.getString("Name"),
                    rs.getString("Department"),
                    rs.getDouble("BaseSalary")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error refreshing employee table: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeManagementSystem::new);
    }
}
