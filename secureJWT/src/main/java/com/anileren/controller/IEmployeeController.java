package com.anileren.controller;

import com.anileren.dto.DtoEmployee;

public interface IEmployeeController {
    public DtoEmployee findEmployeById(Long id);
}
