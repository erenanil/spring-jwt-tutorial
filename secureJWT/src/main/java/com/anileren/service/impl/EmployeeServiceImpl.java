package com.anileren.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anileren.dto.DtoDepartment;
import com.anileren.dto.DtoEmployee;
import com.anileren.model.Department;
import com.anileren.model.Employee;
import com.anileren.repository.EmployeeRepository;
import com.anileren.service.IEmployeeService;

@Service
public class EmployeeServiceImpl implements IEmployeeService{

    @Autowired
    EmployeeRepository employeeRepository;     

    @Override
    public DtoEmployee findEmployeById(Long id) {
        DtoEmployee dtoEmployee = new DtoEmployee();
        DtoDepartment dtoDepartment = new DtoDepartment();

        Optional<Employee> optinal = employeeRepository.findById(id);

        if(optinal.isEmpty()){
            //Exception fırlatılacak.
            return null;
        }

        Employee employeeDb = optinal.get();
        Department departmentDb = employeeDb.getDepartment();

        BeanUtils.copyProperties(employeeDb, dtoEmployee);
        BeanUtils.copyProperties(departmentDb, dtoDepartment);

        dtoEmployee.setDtoDepartment(dtoDepartment);

        return dtoEmployee;
    }
    
}
