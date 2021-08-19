package com.lp.akka.notes.practice;

import java.io.Serializable;

/**
 * @author li.pan
 * @title 员工实体类
 */
public class Employee implements Serializable {
    private float salary;

    private String name;

    public Employee(float salary, String name) {
        this.salary = salary;
        this.name = name;
    }

    public float getSalary() {
        return salary;
    }

    public String getName() {
        return name;
    }
}
