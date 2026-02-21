package com.excel.controller;

import com.excel.dto.Country;
import com.excel.dto.ExportToExcelResponse;
import com.excel.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.excel.util.ExcelUtil.getBase64StringRepsonse;

@RestController
@RequestMapping("/admin")
public class ExcelController {


    @Autowired
    ExcelService excelService;



    @GetMapping("/export")
    public ResponseEntity<ExportToExcelResponse> exportCountryData(){
        return getBase64StringRepsonse(excelService.exportCountryData());
    }

    @GetMapping("/export/{countryCode}")
    public ResponseEntity<ExportToExcelResponse> exportCountryData(@PathVariable String countryCode){
        return getBase64StringRepsonse(excelService.exportCountryData(countryCode));
    }

    @GetMapping("/country/{countryCode}")
    public ResponseEntity<Country> getCountryData(@PathVariable Long countryCode){
        return ResponseEntity.ok(excelService.getCountryData(countryCode));
    }

    @GetMapping("/country")
    public ResponseEntity<List<Country>> getAllCountryData(){
        return ResponseEntity.ok(excelService.getAllCountryData());
    }




}
