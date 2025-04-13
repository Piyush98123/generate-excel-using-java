package com.excel.util;


import com.excel.dto.ExportToExcelResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.*;


public class ExcelUtil {

    private  static String CALIBRI ="Calibri";

    private  static String ARGBHEX1 ="#000000";
    private static String ARGBHEX="#d3d3d3";

    public static final String getReportHeader(String... moduleNames) {

        StringBuilder tmpHeader = new StringBuilder();

        if (moduleNames != null) {
            for (int counter = 0; counter < moduleNames.length; counter++) {
                tmpHeader.append(moduleNames[counter]);
                if (counter < (moduleNames.length - 1)) {
                    tmpHeader.append("|");
                }
            }
        }

        return tmpHeader.toString();
    }

    public static final XSSFCellStyle getHeaderCellStyle(XSSFWorkbook workbook) {

        XSSFFont headerFont = (XSSFFont) workbook.createFont();
        headerFont.setFontName(CALIBRI);
        headerFont.setFontHeightInPoints((short) 15);
        headerFont.setUnderline(org.apache.poi.ss.usermodel.Font.U_SINGLE);

        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setAlignment(CENTER);
        headerCellStyle.setFont(headerFont);

        return headerCellStyle;
    }

    public static final XSSFCellStyle cellBorder(XSSFWorkbook workbook, boolean cellBorder){
        XSSFCellStyle fieldValueCellStyle = workbook.createCellStyle();
        if(cellBorder){
            fieldValueCellStyle.setBorderRight(BorderStyle.THIN);
            fieldValueCellStyle.setBorderLeft(BorderStyle.THIN);
            fieldValueCellStyle.setBorderTop(BorderStyle.THIN);
            fieldValueCellStyle.setBorderBottom(BorderStyle.THIN);
            fieldValueCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            fieldValueCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            fieldValueCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            fieldValueCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            fieldValueCellStyle.setWrapText(true);
        }
        return fieldValueCellStyle;
    }

    public static final XSSFCellStyle getFieldNameCellStyle(XSSFWorkbook workbook,
                                                            HorizontalAlignment alignment) {
        XSSFColor foregroundColor = new XSSFColor(workbook.getStylesSource().getIndexedColors());
        foregroundColor.setARGBHex(ARGBHEX1.substring(1));

        XSSFColor backgroundColor = new XSSFColor(workbook.getStylesSource().getIndexedColors());
        backgroundColor.setARGBHex(ARGBHEX.substring(1));

        XSSFFont fieldNameCellFont = (XSSFFont) workbook.createFont();
        fieldNameCellFont.setFontName(CALIBRI);
        fieldNameCellFont.setFontHeightInPoints((short) 11);
        fieldNameCellFont.setColor(foregroundColor);

        XSSFCellStyle fieldNameCellStyle = cellBorder(workbook,true);
        fieldNameCellStyle.setAlignment(alignment);
        fieldNameCellStyle.setFont(fieldNameCellFont);
        fieldNameCellStyle.setFillForegroundColor(backgroundColor);
        fieldNameCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return fieldNameCellStyle;
    }

    public static final XSSFCellStyle getFieldValueCellStyle(XSSFWorkbook workbook,
                                                             HorizontalAlignment alignment) {
        XSSFColor foregroundColor = new XSSFColor(workbook.getStylesSource().getIndexedColors());
        foregroundColor.setARGBHex("#000000".substring(1));

        XSSFFont fieldValueCellFont = (XSSFFont) workbook.createFont();
        fieldValueCellFont.setFontName(CALIBRI);
        fieldValueCellFont.setFontHeightInPoints((short) 11);
        fieldValueCellFont.setColor(foregroundColor);
        XSSFCellStyle fieldValueCellStyle = workbook.createCellStyle();
        fieldValueCellStyle.setAlignment(alignment);
        fieldValueCellStyle.setFont(fieldValueCellFont);

        return fieldValueCellStyle;
    }

    public static final int addSearchFilters(Map<String, Object> keyValues, XSSFSheet sheet,
                                             int rowNum, final XSSFCellStyle fieldNameRightAlignedCellStyle,
                                             final XSSFCellStyle fieldValueLeftAlignedCellStyle) {

        AtomicInteger rowCount = new AtomicInteger(rowNum);
        AtomicInteger columnCount = new AtomicInteger(0);
        AtomicInteger currentRowNum = new AtomicInteger();
        fieldValueLeftAlignedCellStyle.setWrapText(true);
        fieldNameRightAlignedCellStyle.setWrapText(true);
        keyValues.entrySet().forEach(keyValue -> {
            Row row = null;

            // We need to create a new row for the first time and after that every time the columnCount is
            // reaches 4, as we need to display 2 Search Filters in each row
            boolean isNewRow = (columnCount.get() == 0 || (columnCount.get() >= 4));

            if (isNewRow) {
                columnCount.set(0);
                row = sheet.createRow(rowCount.get());
                currentRowNum.set(rowCount.getAndIncrement());
            } else {
                row = sheet.getRow(currentRowNum.get());
            }

            if (isNotBlank(keyValue.getKey())) {
                Cell cell = row.createCell(columnCount.getAndIncrement());
                cell.setCellValue(keyValue.getKey());
                cell.setCellStyle(fieldNameRightAlignedCellStyle);

                cell = row.createCell(columnCount.getAndIncrement());
                cell.setCellValue(String.valueOf(keyValue.getValue()));
                cell.setCellStyle(fieldValueLeftAlignedCellStyle);
            } else {
                Cell cell = row.createCell(columnCount.getAndIncrement());
                cell = row.createCell(columnCount.getAndIncrement());
            }
        });
        sheet.createRow(rowCount.getAndIncrement());
        return rowCount.get();
    }

    public static final void addColumnHeaders(Row row, List<String> headerNames,
                                              XSSFCellStyle valueCellStyle) {
        AtomicInteger counter = new AtomicInteger(0);
        headerNames.stream().forEach(header -> {
            Cell cell = row.createCell(counter.get());
            cell.setCellValue(headerNames.get(counter.get()));
            cell.setCellStyle(valueCellStyle);
            counter.getAndIncrement();
        });
    }

    public static final void addColumnValues(
            XSSFSheet sheet,
            List<List<Object>> records,
            int rowNum,
            XSSFCellStyle valueCellStyle,
            XSSFWorkbook workbook,
            boolean cellBorder) {
        AtomicInteger rowCount = new AtomicInteger(rowNum);
        XSSFCellStyle fieldValueCellStyleText = cellBorder(workbook, cellBorder);
        fieldValueCellStyleText.setFont(valueCellStyle.getFont());
        fieldValueCellStyleText.setAlignment(LEFT);
        records.stream()
                .forEach(
                        item -> {
                            Row row = sheet.createRow(rowCount.getAndIncrement());
                            AtomicInteger columnCount = new AtomicInteger(0);
                            item.stream()
                                    .forEach(
                                            header -> {
                                                Cell cell = row.createCell(columnCount.get());
                                                String value = String.valueOf(item.get(columnCount.get()));
                                                cell.setCellStyle(fieldValueCellStyleText);
                                                cell.setCellValue(value);
                                                columnCount.getAndIncrement();
                                            });
                        });
    }


    public static final File export(String reportName, String excelExportPath,
                                              String excelExtension, Map<String, Object> searchFilters, String header,
                                              List<String> headerNames,
                                              List<List<Object>> records) {
        int rowNum = 0;
        int columnCount = headerNames.size();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(reportName);
            XSSFCellStyle headerCellStyle = getHeaderCellStyle(workbook);
            XSSFCellStyle fieldNameRightAlignedCellStyle = getFieldNameCellStyle(workbook, RIGHT);
            XSSFCellStyle fieldNameCenterAlignedCellStyle = getFieldNameCellStyle(workbook, CENTER);
            XSSFCellStyle fieldValueCenterAlignedCellStyle = getFieldValueCellStyle(workbook, CENTER);
            XSSFCellStyle fieldValueLeftAlignedCellStyle = getFieldValueCellStyle(workbook, LEFT);

            // Header
            Row worksheetRow = sheet.createRow(rowNum++);
            Cell cell = worksheetRow.createCell(0);
            cell.setCellValue(header);
            cell.setCellStyle(headerCellStyle);
            rowNum++;

            // Export Date
            worksheetRow = sheet.createRow(rowNum++);
            cell = worksheetRow.createCell(0);
            cell.setCellValue("Export Date");
            cell.setCellStyle(fieldNameRightAlignedCellStyle);
            sheet.setColumnWidth(0, 3800);

            cell = worksheetRow.createCell(1);
            cell.setCellValue(String.valueOf(LocalDate.now().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            cell.setCellStyle(fieldValueLeftAlignedCellStyle);
            rowNum++;

            rowNum = addSearchFilters(searchFilters, sheet, rowNum, fieldNameRightAlignedCellStyle,
                    fieldValueLeftAlignedCellStyle);

            // Add column headers
            worksheetRow = sheet.createRow(rowNum++);
            addColumnHeaders(worksheetRow, headerNames, fieldNameCenterAlignedCellStyle);

            // Add data
            addColumnValues(sheet, records, rowNum, fieldValueCenterAlignedCellStyle, workbook, true);

            // Add required formatting
            IntStream.range(1, headerNames.size()).forEach(sheet::autoSizeColumn);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (columnCount - 1)));
            sheet.setActiveCell(new CellAddress(20, 20));
            sheet.setDisplayGridlines(false);

            reportName = reportName + Instant.now().toEpochMilli();
            File file = createFileOnDisk(reportName, excelExportPath, excelExtension);
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to export to excel due to an error");
        }
    }

    public static final File createFileOnDisk(String fileName, String filePath,
                                              String fileExtension) {

        boolean isFolderCreated = false;
        boolean isFileCreated = false;

        File folder = new File(filePath);
        if (!folder.exists()) {
            isFolderCreated = folder.mkdir();
            if (!isFolderCreated) {
                throw new RuntimeException("Could not create folder filePath");
            }
        }

        String reportFilename =
                new StringBuilder().append(filePath).append(fileName).append(fileExtension).toString();

        File file = new File(reportFilename);
        try {
            isFileCreated = file.createNewFile();
            if (!isFileCreated) {
                throw new RuntimeException("Could not create file '%s' with extension '%s' at '%s'".formatted(fileName, fileExtension, filePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Server error");
        }

        return file;
    }

    public static final ResponseEntity<ExportToExcelResponse> getBase64StringRepsonse(
            ExportToExcelResponse response){

        File generatedExcel = response.getFile();
        try{
            byte[] fileContent = Files.readAllBytes(generatedExcel.toPath());
            response.setStream(Base64.getEncoder().encodeToString(fileContent));
            response.setFile(null);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return ResponseEntity.ok().body(response);
    }

}
