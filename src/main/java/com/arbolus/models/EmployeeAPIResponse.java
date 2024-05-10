package com.arbolus.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeAPIResponse {

    private String status;
    private List<Employee> data;
}
