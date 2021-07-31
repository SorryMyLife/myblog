package org.myblog.blog.extend.extract;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.myblog.blog.entity.BlogAdminTableColumns;
import org.myblog.blog.entity.BlogAdminUser;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ExtractPDF {

    public ExtractPDF(){
        extractExcel=new ExtractExcel();
    }

    public ExtractPDF(ExtractExcel extractExcel) {
        this.extractExcel = extractExcel;
    }

    public void setExtractExcel(ExtractExcel extractExcel) {
        this.extractExcel = extractExcel;
    }

    private ExtractExcel extractExcel;

    public String getOutPdfName() {
        return outPdfName;
    }

    private String outPdfName;

    public InputStream ExcelToPdf(InputStream input_document,String outPdfPath) throws IOException, DocumentException {
        // Read workbook into HSSFWorkbook
        HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document);
        // Read worksheet into HSSFSheet
        HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
        // To iterate over the rows
        Iterator<Row> rowIterator = my_worksheet.iterator();
        //We will create output PDF document objects at this point
        Document iText_xls_2_pdf = new Document();
        PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream(outPdfPath));
        iText_xls_2_pdf.open();
        //we have two columns in the Excel sheet, so we create a PDF table with two columns
        //Note: There are ways to make this dynamic in nature, if you want to.
        PdfPTable my_table = new PdfPTable(my_worksheet.getLastRowNum());
        //We will use the object below to dynamically add new data to the table
        PdfPCell table_cell;
        //配置字体选择器，自动推导excel表里的字体类型
        FontSelector selector = new FontSelector();
        selector.addFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 12));
        selector.addFont(FontFactory.getFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED, 10));
        //设置pdf页面默认宽度
        my_table.setWidthPercentage(110);
        float fff[] = new float[my_worksheet.getLastRowNum()];
        for (int i = 0; i < fff.length; i++) {
            fff[i]=0.2f;
        }
        my_table.setWidths(fff);
        //Loop through rows.
        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while(cellIterator.hasNext()) {
                Cell cell = cellIterator.next(); //Fetch CELL
                //设置pdf每个单元格的字体样式
                Phrase phrase = selector.process(cell.getStringCellValue());
                table_cell=new PdfPCell(phrase);
                //feel free to move the code below to suit to your needs
                my_table.addCell(table_cell);
                //next line
            }
        }
        //Finally add the table to PDF document
        iText_xls_2_pdf.add(my_table);
        iText_xls_2_pdf.close();
        //we created our pdf file..
        input_document.close(); //close xls
        return new FileInputStream(new File(outPdfPath));
    }

    public InputStream ExcelToPdf(String excelFilePath,String outPdfPath) throws IOException, DocumentException {
        File file = new File(excelFilePath);
        if(file.exists()){
            File outPdf = new File(outPdfPath);
            if(outPdf.exists()){
                outPdf.delete();
            }
            FileInputStream input_document = new FileInputStream(file);
            return ExcelToPdf(input_document,outPdfPath);
        }

        return null;
    }

    private String getPDFName(File file){
        String name = file.getName();
        if (name.endsWith(".xls")) {
            name = name.substring(0, name.lastIndexOf(".") + 1);
            name += "pdf";
            this.outPdfName=name;
            return file.getParent() + "/" + name;
        }
        return null;
    }

    public InputStream ExcelToPdf(InputStream inputStream) throws IOException, DocumentException {
        File excelFile = extractExcel.getExcelFile();
        String fileName=getPDFName(excelFile);
        return ExcelToPdf(inputStream,fileName);
    }

    public InputStream blodAdminUserPDF(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminUsers){
        InputStream inputStream = extractExcel.blodAdminUserExcel(fileName, adminUser, blogAdminTableColumns, adminUsers);
        try {
            return ExcelToPdf(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    public InputStream blodAdminArticlePDF(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminArticles) {
        InputStream inputStream = extractExcel.blodAdminArticleExcel(fileName, adminUser, blogAdminTableColumns, adminArticles);
        try {
            return ExcelToPdf(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminFilePDF(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminFiles) {
        InputStream inputStream = extractExcel.blodAdminFileExcel(fileName, adminUser, blogAdminTableColumns, adminFiles);
        try {
            return ExcelToPdf(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminRolePDF(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminRole) {
        InputStream inputStream = extractExcel.blodAdminRoleExcel(fileName, adminUser, blogAdminTableColumns, adminRole);
        try {
            return ExcelToPdf(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminCommitPDF(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminCommit) {
        InputStream inputStream = extractExcel.blodAdminCommitExcel(fileName, adminUser, blogAdminTableColumns, adminCommit);
        try {
            return ExcelToPdf(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminRoleUserPDF(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminRoleUser) {
        InputStream inputStream = extractExcel.blodAdminRoleUserExcel(fileName, adminUser, blogAdminTableColumns, adminRoleUser);
        try {
            return ExcelToPdf(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

}
