package com.anileren.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoEmployee {
    
    private String firstName;

    private String lastName;

    private DtoDepartment dtoDepartment;
}
