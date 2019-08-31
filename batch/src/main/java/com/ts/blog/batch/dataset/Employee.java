package com.ts.blog.batch.dataset;

/**
 * Created by rahul on 8/25/19.
 */
public class Employee {

    private Integer id;
    private  String name;

    public Employee(){}

    public Employee(String[] fields){
        id=Integer.getInteger(fields[0]);
        name=fields[1];

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        return id.equals(employee.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
