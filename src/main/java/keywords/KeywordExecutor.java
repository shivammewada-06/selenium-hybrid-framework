package keywords;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  // Added: Missing import for XSSFWorkbook
import utils.ExcelUtils;

import java.io.IOException;
import java.io.InputStream;

public class KeywordExecutor {

    private static final Logger logger = LogManager.getLogger(KeywordExecutor.class);

    /**
     * Executes keywords from keywordSheet.xlsx by reading the specified sheet and performing actions.
     * Assumes the sheet has columns in order: Action, LocatorType, LocatorValue, TestData.
     * Skips the first row if it's headers.
     *
     * @param sheetName The name of the sheet in keywordSheet.xlsx to execute.
     */
    public static void executeKeywords(String sheetName) {
        logger.info("Starting keyword execution from sheet: " + sheetName);

        Object[][] data = null;
        try {
            // Use a similar approach to ExcelUtils but for keywordSheet.xlsx
            data = getKeywordData(sheetName);
        } catch (Exception e) {
            logger.error("Failed to load keyword data from sheet: " + sheetName, e);
            throw new RuntimeException("Error loading keyword data", e);
        }

        if (data == null || data.length == 0) {
            logger.warn("No keyword data found in sheet: " + sheetName);
            return;
        }

        // Assume first row is headers, start from index 1
        for (int i = 1; i < data.length; i++) {
            try {
                String action = (String) data[i][0];
                String locatorType = (String) data[i][1];
                String locatorValue = (String) data[i][2];
                String testData = data[i].length > 3 ? (String) data[i][3] : "";

                logger.info("Executing keyword: Action=" + action + ", LocatorType=" + locatorType + ", LocatorValue=" + locatorValue + ", TestData=" + testData);
                ActionKeywords.performAction(action, locatorType, locatorValue, testData);
            } catch (Exception e) {
                logger.error("Error executing keyword at row " + (i + 1) + ": " + e.getMessage(), e);
                // Continue to next keyword for scalability (graceful error handling)
                // If you want to stop on error, uncomment the next line:
                // throw new RuntimeException("Keyword execution failed at row " + (i + 1), e);
            }
        }

        logger.info("Keyword execution completed for sheet: " + sheetName);
    }

    /**
     * Reads keyword data from keywordSheet.xlsx.
     * This is a helper method similar to ExcelUtils.getTestData but for keywordSheet.xlsx.
     *
     * @param sheetName The sheet name.
     * @return Object[][] of keyword data.
     */
    private static Object[][] getKeywordData(String sheetName) {
        Object[][] data = null;

        try (InputStream is = KeywordExecutor.class.getClassLoader().getResourceAsStream("keywordSheet.xlsx");
             Workbook workbook = new XSSFWorkbook(is)) {

            if (is == null) {
                throw new RuntimeException("keywordSheet.xlsx not found in src/test/resources");
            }

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            if (rowCount <= 0) {
                return new Object[0][0];
            }

            data = new Object[rowCount][];
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    data[i] = new Object[0];
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
            throw new RuntimeException("Error reading keywordSheet.xlsx: " + e.getMessage(), e);
        }

        return data;
    }

    /**
     * Helper method to get cell value, similar to ExcelUtils.
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