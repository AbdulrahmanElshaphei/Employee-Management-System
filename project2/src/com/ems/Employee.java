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
public class Employee {
    private int id;
    private String name;
    private String type;
    private double salary;

    public Employee(int id, String name, String type, double salary) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.salary = salary;
    }

    // Getters and toString()
    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Type: " + type + ", Salary: " + salary;
    }
}
