package org.example;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome to TOC Creator!");

        PdfTocCreator tocCreator = new PdfTocCreator();

        PdfTocCreatorV1 tocCreatorV1 = new PdfTocCreatorV1();

        // Path to your PDF file
        String pdfFilePath = "/Users/sahilkapoor/downloads/samplepdf2.pdf";
        String samplePdfV2 =  "/Users/sahilkapoor/downloads/samplepdfV2.pdf";
        String pdfFileSummaryPath = "/Users/sahilkapoor/downloads/samplepublicmarketsummary.pdf";

        // Generate the TOC by extracting the first line of each page
//        tocCreator.generateToc(pdfFilePath);
        tocCreatorV1.generateToc(samplePdfV2);
    }
}