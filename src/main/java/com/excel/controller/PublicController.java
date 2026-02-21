package com.excel.controller;

import com.excel.dto.Country;
import com.excel.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    ExcelService excelService;

    @GetMapping("/country")
    public ResponseEntity<List<Country>> getAllCountryData(){
        return ResponseEntity.ok(excelService.getAllCountryData());
    }
}
