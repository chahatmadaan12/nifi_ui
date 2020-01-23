package com.applicate.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	enum Excel{
		XLS(".xls"),
		XLSX(".xlsx");
		
		private String extension;
		
	    Excel(String extension) {
	    	this.extension = extension;
		}
	    
	    public String getExtension() {
			return extension;
		}
	    
	    public static Excel getExcel(String extension){
	    	for (Excel excel : Excel.values()) {
				if(extension.equalsIgnoreCase(excel.getExtension())){
					return excel;
				}
			}
	    	return null;
	    }
	    
	}
	
	public static final List<String> supportedContentTypes = Arrays.asList("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","application/vnd.ms-excel"); 

	private String getContentType(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        if(extension.equalsIgnoreCase(".xlsx")) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        if(extension.equalsIgnoreCase(".xls")) {
            return "application/vnd.ms-excel";
        }
        return "";
    }
	
	public static Workbook createWorkbook(Excel excel) {
		return createWorkbook(excel,null);
	}
	
	public static Workbook createWorkbook(Excel excel, String sheetName) {
        Workbook workbook = null;
        if (excel == Excel.XLS) {
            workbook = new HSSFWorkbook();
        }
        if (excel == Excel.XLSX) {
            workbook = new XSSFWorkbook();
        }
        if(sheetName!=null)
           workbook.createSheet(sheetName);
        else
           workbook.createSheet();
        return workbook;
    }
	
}
