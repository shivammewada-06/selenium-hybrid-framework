package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class ExcelUtils {

    /**
     * Reads test data from testdata.xlsx located in src/test/resources.
     * Returns a 2D Object array suitable for TestNG DataProvider.
     * Assumes the first row is data (no headers skipped); adjust if needed.
     *
     * @param sheetName The name of the sheet to read from.
     * @return Object[][] containing the test data.
     * @throws RuntimeException if the file, sheet, or reading fails.
     */
    public static Object[][] getTestData(String sheetName) {
        Object[][] data = null;

        try (InputStream is = ExcelUtils.class.getClassLoader().getResourceAsStream("testdata.xlsx");
             Workbook workbook = new XSSFWorkbook(is)) {

            if (is == null) {
                throw new RuntimeException("testdata.xlsx not found in src/test/resources");
            }

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            if (rowCount <= 0) {
                return new Object[0][0]; // Return empty array if no data
            }

            // Initialize data array
            data = new Object[rowCount][];
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    data[i] = new Object[0]; // Handle null rows
                    continue;
                }
                int cellCount = row.getPhysicalNumberOfCells();
                data[i] = new Object[cellCount];
                for (int j = 0; j < cellCount; j++) {
                    Cell cell = row.getCell(j);
                    data[i][j] = getCellValue(cell);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
        }

        return data;
    }

    /**
     * Helper method to get the value of a cell based on its type.
     *
     * @param cell The cell to read.
     * @return The cell value as an Object (String, Double, Boolean, etc.).
     */
    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }
}