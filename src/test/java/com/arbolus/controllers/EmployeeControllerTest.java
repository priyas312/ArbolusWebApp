package com.arbolus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.arbolus.exceptions.EmployeeFetchException;
import com.arbolus.models.Employee;
import com.arbolus.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static java.util.Optional.of;
import static org.assertj.core.util.Lists.list;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void Test_GetOldestEmployee() throws Exception {
        //Given
        final Employee emp = Employee.builder().id(1).build();
        final List<Employee> employees = list(emp);
        final String response = objectMapper.writeValueAsString(emp);

        //When
        when(service.getEmployees()).thenReturn(employees);
        when(service.getOldestEmployee(employees)).thenReturn(of(emp));

        //Test
        mvc.perform(MockMvcRequestBuilders.get("/employee/oldest").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(response)));
    }

    @Test
    public void Test_GetOldestEmployee_WhenThereIsError() throws Exception {
        //Given
        final String errorResponse = "Error while fetching employee details";

        //When
        doThrow(new EmployeeFetchException(errorResponse))
                .when(service).getEmployees();

        //Test
        mvc.perform(MockMvcRequestBuilders.get("/employee/oldest").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo(errorResponse)));
    }
}