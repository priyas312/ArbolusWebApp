package com.arbolus.models;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Employee {

    private int id;
    private String name;
    private int salary;
    private int age ;
    private String profileImage;
}
