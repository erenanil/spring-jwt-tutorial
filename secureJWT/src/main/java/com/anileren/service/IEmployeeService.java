package com.anileren.service;

import com.anileren.dto.DtoEmployee;

public interface IEmployeeService {
    public DtoEmployee findEmployeById(Long id);
    
}
