package com.projects.coaching_offline_support.common.Service.impl;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Service
public class ExcelExportService {

    public <T> ByteArrayInputStream export(
            String sheetName,
            List<String> headers,
            List<T> data,
            Function<T, List<Object>> rowMapper
    ) throws IOException {

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {

            Sheet sheet = workbook.createSheet(sheetName);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < headers.size(); i++) {
                headerRow.createCell(i).setCellValue(headers.get(i));
            }

            // Data
            int rowNum = 1;

            for (T item : data) {

                Row row = sheet.createRow(rowNum++);

                List<Object> values = rowMapper.apply(item);

                for (int col = 0; col < values.size(); col++) {

                    Cell cell = row.createCell(col);

                    Object value = values.get(col);

                    if (value == null) {
                        cell.setBlank();
                    } else if (value instanceof Number n) {
                        cell.setCellValue(n.doubleValue());
                    } else if (value instanceof Boolean b) {
                        cell.setCellValue(b);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }

            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
