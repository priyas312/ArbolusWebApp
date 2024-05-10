package com.arbolus.Service;

import com.arbolus.exceptions.EmployeeFetchException;
import com.arbolus.models.Employee;
import com.arbolus.services.EmployeeService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.arbolus.services.EmployeeService.URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatusCode.valueOf;

@SpringBootTest
public class EmployeeServiceTest {

    @MockBean
    private RestTemplate template;

    @Autowired
    @InjectMocks
    private EmployeeService service;

    @Test
    void TestGetEmployees_WhenAPIReturnSuccessResponse(){
        // Given
        final String apiResponse = "{\"status\":\"success\",\"data\":[{\"id\":1,\"employee_name\":\"Tiger Nixon\",\"employee_salary\":320800,\"employee_age\":61,\"profile_image\":\"\"},{\"id\":2,\"employee_name\":\"Garrett Winters\",\"employee_salary\":170750,\"employee_age\":63,\"profile_image\":\"\"}]}";
        final ResponseEntity<String> responseEntity = new ResponseEntity<>(apiResponse, valueOf(200));

        //When
        when(template.getForEntity(URL, String.class)).thenReturn(responseEntity );

        //Test
        List<Employee> employeeList = service.getEmployees();

        //Then
        assertNotNull(employeeList);
        assertEquals(2, employeeList.size());
    }

    @Test
    void TestGetEmployees_WhenAPIReturnError(){
        // Given
        final String apiResponse = "too many request";
        final ResponseEntity<String> responseEntity = new ResponseEntity<>(apiResponse, valueOf(405));

        //When
        when(template.getForEntity(URL, String.class)).thenReturn(responseEntity );

        //Test
        assertThrows(EmployeeFetchException.class, () -> service.getEmployees());
    }

    @Test
    void TestGetOldestEmployee(){
        //Given
        final List<Employee> employees= createEmployeeList();

        //Test
        Optional<Employee> oldestEmployee = service.getOldestEmployee(employees);

        //Then
        assertTrue(oldestEmployee.isPresent());
        assertEquals(oldestEmployee.get(), employees.get(2));
    }

    @Test
    void TestSecondOldestEmployee(){
        //Given
        final List<Employee> employees= createEmployeeList();

        //Test
        Optional<Employee> oldestEmployee = service.getSecondOldest(employees);

        //Then
        assertTrue(oldestEmployee.isPresent());
        assertEquals(oldestEmployee.get(), employees.get(0));
    }

    @Test
    void TestSecondOldestEmployee_WhenSingleEmployee(){
        //Given
        final List<Employee> employees= Lists.list(Employee.builder()
                .id(1)
                .age(31)
                .build());

        //Test
        Optional<Employee> oldestEmployee = service.getSecondOldest(employees);

        //Then
        assertFalse(oldestEmployee.isPresent());
    }

    @Test
    void TestSecondOldestEmployee_WhenEmployeeListIsEmpty(){
        //Given
        final List<Employee> employees= new ArrayList<>();

        //Test
        Optional<Employee> oldestEmployee = service.getSecondOldest(employees);

        //Then
        assertFalse(oldestEmployee.isPresent());
    }

    private List<Employee> createEmployeeList(){
        final Employee emp1 = Employee.builder()
                .id(1)
                .age(31)
                .build();
        final Employee emp2 = Employee.builder()
                .id(2)
                .age(25)
                .build();
        final Employee emp3 = Employee.builder()
                .id(3)
                .age(52)
                .build();
        return Lists.list(emp1, emp2, emp3);
    }
}
