package com.anileren.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anileren.controller.IEmployeeController;
import com.anileren.dto.DtoEmployee;
import com.anileren.service.IEmployeeService;


@RestController
@RequestMapping("/employee")
public class RestEmployeeController implements IEmployeeController {

    @Autowired
    IEmployeeService employeeService;

    @GetMapping("/{id}")
    public DtoEmployee findEmployeById(@PathVariable(value = "id") Long id) {
        return employeeService.findEmployeById(id);        
    }
    
    
    
}
