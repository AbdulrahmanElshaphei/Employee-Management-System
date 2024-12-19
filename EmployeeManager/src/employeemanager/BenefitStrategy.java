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

// ================= Strategy Design Pattern ================== //

// Strategy Interface
interface BenefitStrategy {
    double calculateBenefits(double baseSalary);
}

// Concrete Strategy - HR Department
class HRBenefitStrategy implements BenefitStrategy {
    @Override
    public double calculateBenefits(double baseSalary) {
        return baseSalary * 0.10; // 10% bonus
    }
}

// Concrete Strategy - IT Department
class ITBenefitStrategy implements BenefitStrategy {
    @Override
    public double calculateBenefits(double baseSalary) {
        return baseSalary * 0.15; // 15% bonus
    }
}

// Concrete Strategy - Finance Department
class FinanceBenefitStrategy implements BenefitStrategy {
    @Override
    public double calculateBenefits(double baseSalary) {
        return baseSalary * 0.12; // 12% bonus
    }
}

// Context Class
class BenefitCalculator {
    private BenefitStrategy strategy;

    public void setStrategy(BenefitStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(double baseSalary) {
        if (strategy == null) {
            throw new IllegalStateException("No benefit strategy set.");
        }
        return strategy.calculateBenefits(baseSalary);
    }
}