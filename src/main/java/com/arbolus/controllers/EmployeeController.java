package com.arbolus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.arbolus.models.Employee;
import com.arbolus.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/oldest")
    public ResponseEntity<String> getOldestEmployee() {
        try {
            List<Employee> employees = service.getEmployees();
            Optional<Employee> employee =  service.getOldestEmployee(employees);
            if (employee.isPresent()) {
                String employeeJson = objectMapper.writeValueAsString(employee.get());
                return ResponseEntity.ok(employeeJson);
            } else {
                return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("Employee Data is Missing");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage());
        }
    }

}