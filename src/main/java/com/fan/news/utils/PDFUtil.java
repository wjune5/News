package com.fan.news.utils;

import com.fan.news.pojo.News;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PDFUtil {
    public static void createPDF(String fileName, News news) {
        Document document = new Document();
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("D:\\JavaProjects\\news_dev\\pdf\\" + fileName));
            document.addAuthor(news.getNickname());
            document.addTitle(news.getTitle());
            document.add(new Paragraph(news.getContent()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
