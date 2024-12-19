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
public class EmployeeFactory {
    public static Employee createEmployee(String name, String type, double salary, int id) {
        return new Employee(id, name, type, salary);
    }
}
