package com.excel.controller;

import com.excel.dto.ExportToExcelResponse;
import com.excel.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
