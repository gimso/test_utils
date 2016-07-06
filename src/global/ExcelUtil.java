package global;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class is a utility for excel file,</br>
 * It parses the *.xlsx file from known path,</br>
 * By workbook sheet row and cell.</br>
 * @author Yehuda Ginsburg
 */
public class ExcelUtil {

	public static final String XLSX_FILE_PATH = "files/TestData.xlsx";
	public static final Integer TYPE_COLUMN = 0;
	public static final Integer TEST_NAME_COLUMN = 1;
	public static final Integer EXPECTED_RESULT_COLUMN = 2;
	
	/**
	 * get workbook
	 * @return the Workbook from XLSX_FILE_PATH
	 */
	private static Workbook getWorkbook() {
		FileInputStream inputStream;
		try {
			String filePath = XLSX_FILE_PATH;
			File f = new File(filePath);
			String path = f.getAbsolutePath();
			inputStream = new FileInputStream(path);
			return new XSSFWorkbook(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get sheet by Workbook and name
	 * @param Workbook workbook
	 * @param String sheetName
	 * @return Sheet
	 */
	private static Sheet getSheetByName(Workbook workbook, String sheetName) {
		return workbook.getSheet(sheetName);
	}

	
	/**
	 * Get all rows of a sheet by Sheet
	 * @param sheet
	 */
	private static List<Row> getAllRows(Sheet sheet) {
		int a = sheet.getPhysicalNumberOfRows();
		List<Row> rows = new ArrayList<Row>();
		for (int i = 0; i < a; i++) {			
			if (sheet.getRow(i).getCell(0)!=null) {
				rows.add(sheet.getRow(i));
			}
		}
		return rows != null ? rows : null;
	}

	/**
	 * Get all cells from a Row
	 * @param Row row
	 * @return List of Cell
	 */
	private static List<Cell> getAllCells(Row row) {
		List<Cell> cells = new ArrayList<Cell>();
		for (Cell cell : row) {
				cells.add(cell);
		}
		return cells != null ? cells : null;
	}
	
	/**
	 * Get all cell values of a sheet By sheet name
	 * 
	 * @param String
	 *            sheetName
	 * @return List of Object's List
	 */
	public static List<Object[]> getAllSheetCells(String sheetName) {
		Sheet sheet = getSheetByName(getWorkbook(), sheetName);
		int maxNumOfCells = sheet.getRow(0).getLastCellNum(); // The the maximum
																// number of
																// columns
		List<Row> rows = getRowListFromSheet(sheet);
		return initizlizedObjectArrayFromRowList( maxNumOfCells, rows);
	}

	/**
	 * Convert from row iterator of sheet to list
	 * @param sheet
	 * @return List of Rows
	 */
	private static List<Row> getRowListFromSheet(Sheet sheet) {
		Iterator<Row> rows = sheet.rowIterator();
		List<Row> rows2List = new ArrayList<Row>();
		while (rows.hasNext()) {
			rows2List.add(rows.next());
		}
		return rows2List;
	}

	/**
	 * get rows from list and convert it to object[]
	 * @param maxNumOfCells
	 * @param List of rows
	 * @return List of Object[]
	 */
	private static List<Object[]> initizlizedObjectArrayFromRowList(
			 int maxNumOfCells, List<Row> rows) {
		List<Object[]> objects = new ArrayList<Object[]>();
		for (Row row : rows) {
			List<Cell> cellsList = new ArrayList<Cell>();
			for (int i = 0; i < rows.get(0).getLastCellNum(); i++) { // Loop
																			// through
																			// cells
				XSSFCell cell = (XSSFCell) row.getCell(i,
						Row.RETURN_BLANK_AS_NULL);
				cellsList.add(cell);
			}
			List<Object> temp = new ArrayList<Object>();
			for (Cell cell : cellsList) {
				Object object = getCellValueInAnyFormat(cell);
				temp.add(object);
			}
			Object[] array = temp.toArray(new Object[maxNumOfCells]);
			// if it's not an empty rows it will add
			if (array[0] != null) {
				objects.add(array);
			}
		}
		return objects;
	}
	
	
	/**
	 * Get Cell value as an Object
	 * @param cell
	 * @return Object value of cell
	 */
	private static Object getCellValueInAnyFormat(Cell cell) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				return cell.getNumericCellValue();
			}

		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();

		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_BLANK:
			return null;
		default:
			return null;
		}
	}

	
}
