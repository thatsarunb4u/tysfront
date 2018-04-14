/**
 * 
 */
package com.digipasar.tyfrontend.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Arun
 *
 */
public class AExcelParser {
	
	private static String NUMERIC_FORMAT = "###############";

	public static List<Map<String,String>> getExcelData(File inpFile,String sheetName){
		
		List<Map<String,String>> rowList = new ArrayList<Map<String,String>>();
		Workbook wb = null;
		
		try {
			wb = new XSSFWorkbook(inpFile);
			int sheetIndex = wb.getSheetIndex(sheetName);
			Sheet sheet = wb.getSheetAt(sheetIndex);
			
			DataFormatter formatter = new DataFormatter();
			
			Row headerRow = sheet.getRow(0);
			List<String> header = new ArrayList<>();
			
			for(Cell cell: headerRow) {
				if("".equals(formatter.formatCellValue(cell).trim())) {
					break;
				}
				header.add(formatter.formatCellValue(cell).trim());
			}
			for(Row row : sheet){
				
				Map<String,String> rowMap = new LinkedHashMap<>();
				
				for(int i=0; i< header.size();i++) {
					Cell cell = row.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
					if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						rowMap.put(header.get(i),new DecimalFormat(NUMERIC_FORMAT).format(cell.getNumericCellValue()));
					}else {
						rowMap.put(header.get(i),formatter.formatCellValue(cell).trim());
					}
					
				}
				
				rowList.add(rowMap);
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally {
			if(wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return rowList;
	}
	
public static List<Map<String,String>> getExcelData(InputStream inpFile,String sheetName){
		
		List<Map<String,String>> rowList = new ArrayList<Map<String,String>>();
		Workbook wb = null;
		
		try {
			wb = new XSSFWorkbook(inpFile);
			int sheetIndex = wb.getSheetIndex(sheetName);
			Sheet sheet = wb.getSheetAt(sheetIndex);
			
			DataFormatter formatter = new DataFormatter();
			
			Row headerRow = sheet.getRow(0);
			List<String> header = new ArrayList<>();
			
			for(Cell cell: headerRow) {
				if("".equals(formatter.formatCellValue(cell).trim())) {
					break;
				}
				header.add(formatter.formatCellValue(cell).trim());
			}
			outer: for(Row row : sheet){
				
				Map<String,String> rowMap = new HashMap<>();
				
				for(int i=0; i< header.size();i++) {
					Cell cell = row.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
					
					//To skip rows with empty first column
					if(i==0 && "".equals(formatter.formatCellValue(cell).trim())) {
						continue outer;
					}
					
					//To skip header record
					if(header.get(i).equals(formatter.formatCellValue(cell).trim())) {
						continue outer;
					}
					if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						rowMap.put(header.get(i),new DecimalFormat(NUMERIC_FORMAT).format(cell.getNumericCellValue()));
					}else {
						rowMap.put(header.get(i),formatter.formatCellValue(cell).trim());
					}
					
				}
				
				rowList.add(rowMap);
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return rowList;
	}
}
