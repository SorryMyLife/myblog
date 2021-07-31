package org.myblog.blog.extend.extract;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.Iterator;

public class ExtractWord {

    public ExtractWord(){
        this.extractExcel =new ExtractExcel();
    }
    private ExtractExcel extractExcel;

    private String outWordName;

    public ExtractExcel getExtractExcel() {
        return extractExcel;
    }

    public void setExtractExcel(ExtractExcel extractExcel) {
        this.extractExcel = extractExcel;
    }

    public String getOutWordName() {
        return outWordName;
    }

    public void setOutWordName(String outWordName) {
        this.outWordName = outWordName;
    }

    private String getWordName(File file){
        String name = file.getName();
        if (name.endsWith(".xls")) {
            name = name.substring(0, name.lastIndexOf(".") + 1);
            name += "doc";
            this.outWordName=name;
            return file.getParent() + "/" + name;
        }
        return null;
    }

    public InputStream ExcelToWord(InputStream inputStream , String outWordPath) throws IOException {
        XWPFDocument document= new XWPFDocument();
        HSSFWorkbook my_xls_workbook = new HSSFWorkbook(inputStream);
        HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
        Iterator<Row> rowIterator = my_worksheet.iterator();
        FileOutputStream out = new FileOutputStream(outWordPath);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            StringBuilder sb = new StringBuilder();
            XWPFRun run = paragraph.createRun();
            run.setText("");
            while(cellIterator.hasNext()) {
                Cell cell = cellIterator.next(); //Fetch CELL
                sb.append(cell.getStringCellValue()+"   ");
            }
            run.setText(sb.toString());
            run.addBreak();
        }
        document.write(out);
        inputStream.close();
        return new FileInputStream(outWordPath);
    }

    public InputStream ExcelToWord(InputStream inputStream) throws IOException {
        File excelFile = extractExcel.getExcelFile();
        String fileName=getWordName(excelFile);
        return ExcelToWord(inputStream,fileName);
    }


}
