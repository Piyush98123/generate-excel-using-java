package com.excel.controller;

import com.excel.dto.Country;
import com.excel.dto.ExportToExcelResponse;
import com.excel.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.excel.util.ExcelUtil.getBase64StringRepsonse;

@RestController
@RequestMapping("/api/v1/excel")
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



}
