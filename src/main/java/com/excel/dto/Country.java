package com.excel.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    private Long countryId;
    private String countryName;
    private String countryCode;
    private Long countryPopulation;
}
