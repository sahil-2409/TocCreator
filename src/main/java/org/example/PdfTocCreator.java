package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitDestination;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PdfTocCreator {

    public void generateToc(String inputPdfPath) {
        PDDocument originalDocumentCompany = null;
//        PDDocument originalDocumentSummary = null;
        PDDocument newDocument = null;

        try {
            originalDocumentCompany = PDDocument.load(new File(inputPdfPath));

            PDFTextStripper pdfStripper = new PDFTextStripper();

            List<String> tocEntries = new ArrayList<>();

            for (int i = 1; i <= originalDocumentCompany.getNumberOfPages(); i++) {
                pdfStripper.setStartPage(i);
                pdfStripper.setEndPage(i);

                String pageText = pdfStripper.getText(originalDocumentCompany);
                if(pageText.equals(" ") || pageText.equals("\n")) {
                    continue;
                }
                String firstLine = pageText.split("\n")[0];

                tocEntries.add("Page " + i + ": " + firstLine);
            }

            System.out.println(tocEntries);

            newDocument = new PDDocument();

            PDPage tocPage = new PDPage();
            newDocument.addPage(tocPage);

            PDPageContentStream contentStream = new PDPageContentStream(newDocument, tocPage);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700); // Position the title
            contentStream.showText("Table of Contents");
            contentStream.endText();
            System.out.println(tocEntries.size());

            int yOffset = 650;
            for (int i = 0; i < tocEntries.size(); i++) {
                System.out.println("Hello Index" + i);
                String entry = tocEntries.get(i);

                contentStream.beginText();
                contentStream.newLineAtOffset(100, yOffset); // Position each entry
                contentStream.showText(entry);
                contentStream.endText();

                PDAnnotationLink link = new PDAnnotationLink();

                System.out.println("Total pages in original document: " + originalDocumentCompany.getNumberOfPages());
                System.out.println("Current index: " + (i + 1));

                if (i + 1 > originalDocumentCompany.getNumberOfPages()) {
                    System.out.println("Attempting to access a page that does not exist");
                    System.out.println(i);
                    System.out.println(originalDocumentCompany.getNumberOfPages());
                    break; // Prevent further execution if this condition occurs
                }
                PDPageFitDestination destination = new PDPageFitDestination();
                destination.setPage(originalDocumentCompany.getPage(i));

                link.setDestination(destination);

                PDRectangle rect = new PDRectangle(100, yOffset - 12, 300, 12);
                link.setRectangle(rect);

                tocPage.getAnnotations().add(link);

                yOffset -= 20;
            }

            contentStream.close();

            for (PDPage page : originalDocumentCompany.getPages()) {
                newDocument.addPage(page);
            }

            newDocument.save("/Users/sahilkapoor/downloads/with_tocV18.pdf"); // Specify the output PDF path

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (originalDocumentCompany != null) {
                    originalDocumentCompany.close();
                }
                if (newDocument != null) {
                    newDocument.close();
                }
            } catch (IOException e) {
                System.err.println("Failed to close documents: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
