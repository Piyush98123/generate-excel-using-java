package com.excel.service;

import com.excel.dto.Country;
import com.excel.dto.ExportToExcelResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.excel.util.ExcelUtil.export;
import static com.excel.util.ExcelUtil.getReportHeader;

@Service
@Slf4j
public class ExcelService {


    public static final List<Country> COUNTRY_LIST = new ArrayList<>();

    static {
        COUNTRY_LIST.add(new Country(1L, "United States", "US", 331000000L));
        COUNTRY_LIST.add(new Country(2L, "India", "IN", 1393000000L));
        COUNTRY_LIST.add(new Country(3L, "China", "CN", 1444000000L));
        COUNTRY_LIST.add(new Country(4L, "Brazil", "BR", 213000000L));
        COUNTRY_LIST.add(new Country(5L, "Russia", "RU", 146000000L));
        COUNTRY_LIST.add(new Country(6L, "Japan", "JP", 126000000L));
        COUNTRY_LIST.add(new Country(7L, "Germany", "DE", 83000000L));
        COUNTRY_LIST.add(new Country(8L, "France", "FR", 67000000L));
        COUNTRY_LIST.add(new Country(9L, "United Kingdom", "UK", 67000000L));
        COUNTRY_LIST.add(new Country(10L, "Italy", "IT", 60000000L));
        COUNTRY_LIST.add(new Country(11L, "Mexico", "MX", 128000000L));
        COUNTRY_LIST.add(new Country(12L, "Indonesia", "ID", 273000000L));
        COUNTRY_LIST.add(new Country(13L, "Pakistan", "PK", 220000000L));
        COUNTRY_LIST.add(new Country(14L, "Bangladesh", "BD", 165000000L));
        COUNTRY_LIST.add(new Country(15L, "Nigeria", "NG", 206000000L));
        COUNTRY_LIST.add(new Country(16L, "Vietnam", "VN", 97000000L));
        COUNTRY_LIST.add(new Country(17L, "Philippines", "PH", 109000000L));
        COUNTRY_LIST.add(new Country(18L, "Turkey", "TR", 84000000L));
        COUNTRY_LIST.add(new Country(19L, "Iran", "IR", 83000000L));
        COUNTRY_LIST.add(new Country(20L, "Thailand", "TH", 70000000L));
        COUNTRY_LIST.add(new Country(21L, "South Korea", "KR", 52000000L));
        COUNTRY_LIST.add(new Country(22L, "Spain", "ES", 47000000L));
        COUNTRY_LIST.add(new Country(23L, "Canada", "CA", 38000000L));
        COUNTRY_LIST.add(new Country(24L, "Australia", "AU", 26000000L));
        COUNTRY_LIST.add(new Country(25L, "Argentina", "AR", 45000000L));
        COUNTRY_LIST.add(new Country(26L, "Colombia", "CO", 51000000L));
        COUNTRY_LIST.add(new Country(27L, "South Africa", "ZA", 60000000L));
        COUNTRY_LIST.add(new Country(28L, "Ukraine", "UA", 41000000L));
        COUNTRY_LIST.add(new Country(29L, "Poland", "PL", 38000000L));
        COUNTRY_LIST.add(new Country(30L, "Malaysia", "MY", 32000000L));
        COUNTRY_LIST.add(new Country(31L, "Saudi Arabia", "SA", 35000000L));
        COUNTRY_LIST.add(new Country(32L, "Peru", "PE", 33000000L));
        COUNTRY_LIST.add(new Country(33L, "Netherlands", "NL", 17000000L));
        COUNTRY_LIST.add(new Country(34L, "Iraq", "IQ", 40000000L));
        COUNTRY_LIST.add(new Country(35L, "Afghanistan", "AF", 39000000L));
        COUNTRY_LIST.add(new Country(36L, "Morocco", "MA", 37000000L));
        COUNTRY_LIST.add(new Country(37L, "Uzbekistan", "UZ", 34000000L));
        COUNTRY_LIST.add(new Country(38L, "Nepal", "NP", 30000000L));
        COUNTRY_LIST.add(new Country(39L, "Venezuela", "VE", 28000000L));
        COUNTRY_LIST.add(new Country(40L, "Ghana", "GH", 31000000L));
    }


    public ExportToExcelResponse exportCountryData(String countryCode) {
        ExportToExcelResponse response = new ExportToExcelResponse();
        response.setExtension(".xlsx");
        String header = getReportHeader("EXPORT DATA");
        Map<String, Object> searchFilters = new LinkedHashMap<>();
        Country countryList = COUNTRY_LIST.stream().filter(country -> country.getCountryCode().equals(countryCode)).findAny().get();
        searchFilters.put("Country id", countryList.getCountryId());
        searchFilters.put("Country name", countryList.getCountryName());
        List<String> headerNames = List.of("Country id", "Country name", "Country code","Country population");
        List<List<Object>> records = new ArrayList<>();
        List<Object> exportRecord = Lists.newArrayList();
        exportRecord.add(countryList.getCountryId());
        exportRecord.add(countryList.getCountryName());
        exportRecord.add(countryList.getCountryCode());
        exportRecord.add(countryList.getCountryPopulation());
        records.add(exportRecord);
        response.setFile(
                export("report", "E:\\tmp\\", ".xlsx", searchFilters, header,
                        headerNames, records));
        return response;
    }

    public ExportToExcelResponse exportCountryData() {
        ExportToExcelResponse response = new ExportToExcelResponse();
        response.setExtension(".xlsx");
        String header = getReportHeader("EXPORT DATA");
        Map<String, Object> searchFilters = new LinkedHashMap<>();
        searchFilters.put("Country id", "");
        searchFilters.put("Country name", "");
        List<String> headerNames = List.of("Country id", "Country name", "Country code","Country population");
        List<List<Object>> records = new ArrayList<>();
        for (Country country : COUNTRY_LIST) {
            List<Object> exportRecord = Lists.newArrayList();
            exportRecord.add(country.getCountryId());
            exportRecord.add(country.getCountryName());
            exportRecord.add(country.getCountryCode());
            exportRecord.add(country.getCountryPopulation());
            records.add(exportRecord);
        }
        response.setFile(
                export("report", "E:\\tmp\\", ".xlsx", searchFilters, header,
                        headerNames, records));
        return response;
    }
}
