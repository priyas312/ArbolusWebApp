package com.arbolus.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.arbolus.exceptions.EmployeeFetchException;
import com.arbolus.models.Employee;
import com.arbolus.models.EmployeeAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class EmployeeService {

    @Autowired
    private RestTemplate template;

    @Autowired
    private ObjectMapper mapper;

    public final static String URL = "https://dummy.restapiexample.com/api/v1/employees";

    public List<Employee> getEmployees(){
        ResponseEntity<String> apiResponse = template.getForEntity(URL, String.class);
        if (apiResponse.getStatusCode().is2xxSuccessful()){
            try {
                EmployeeAPIResponse response = mapper.readValue(apiResponse.getBody(), EmployeeAPIResponse.class);
                return response.getData();
            }catch(JsonProcessingException ex){
                throw new EmployeeFetchException("error reading employee Information");
            }
        }else {
            throw new EmployeeFetchException("Can not get employee Information : " + apiResponse.getBody());
        }
    }

    public Optional<Employee> getOldestEmployee(List<Employee> employees){
        return employees.stream().max(Comparator.comparing(Employee::getAge));
    }

    public Optional<Employee> getSecondOldest(List<Employee> employees){
        Optional<Employee> secondOldest = Optional.empty();
        if (employees == null || employees.size() < 2){
            return secondOldest;
        }
        Employee oldest = null;
        for (Employee e: employees){
            if (oldest == null){
                oldest = e;
            }else{
                if (e.getAge() < oldest.getAge()){
                    secondOldest = Optional.of(oldest);
                    oldest = e;
                }
            }
        }
        return secondOldest;
    }
}
