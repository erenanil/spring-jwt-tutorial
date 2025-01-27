package com.anileren.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anileren.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    
}
