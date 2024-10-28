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

public class PdfTocCreatorV1 {
    public void generateToc(String inputPdfPath) {
        PDDocument originalDocument = null;
        PDDocument newDocument = null;

        try {
            originalDocument = PDDocument.load(new File(inputPdfPath));
            PDFTextStripper pdfStripper = new PDFTextStripper();

            List<String> tocEntries = new ArrayList<>();
            int numberOfOriginalPages = originalDocument.getNumberOfPages();

            // Generate TOC entries without page numbers initially
            for (int i = 1; i <= numberOfOriginalPages; i++) {
                pdfStripper.setStartPage(i);
                pdfStripper.setEndPage(i);

                String pageText = pdfStripper.getText(originalDocument);
                if (pageText.trim().isEmpty()) {
                    continue;
                }

                String firstLine = pageText.split("\n")[0];
                tocEntries.add(firstLine);  // Add entry without page number initially
            }

            // Calculate required TOC pages based on entries and limit per page
            int entriesPerPage = 4;  // Example limit per TOC page
            int tocPagesCount = (int) Math.ceil((double) tocEntries.size() / entriesPerPage);

            newDocument = new PDDocument();
            int currentEntry = 0;

            // Create TOC pages with correct page numbers for entries
            for (int tocPageNumber = 1; tocPageNumber <= tocPagesCount; tocPageNumber++) {
                PDPage tocPage = new PDPage();
                newDocument.addPage(tocPage);

                PDPageContentStream contentStream = new PDPageContentStream(newDocument, tocPage);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Table of Contents");
                contentStream.endText();

                int yOffset = 650;
                for (int i = 0; i < entriesPerPage && currentEntry < tocEntries.size(); i++, currentEntry++) {
                    String entry = tocEntries.get(currentEntry);

                    // Adjusted page number: offset by the TOC page count
                    int adjustedPageNumber = tocPagesCount + currentEntry + 1;
                    entry = entry + " ............ " + adjustedPageNumber;

                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, yOffset);
                    contentStream.showText(entry);
                    contentStream.endText();

                    PDAnnotationLink link = new PDAnnotationLink();
                    PDPageFitDestination destination = new PDPageFitDestination();
                    destination.setPage(originalDocument.getPage(currentEntry));

                    link.setDestination(destination);
                    PDRectangle rect = new PDRectangle(100, yOffset - 12, 300, 12);
                    link.setRectangle(rect);
                    tocPage.getAnnotations().add(link);

                    yOffset -= 20;
                }
                contentStream.close();
            }

            // Add all original content pages after TOC pages
            for (PDPage page : originalDocument.getPages()) {
                newDocument.addPage(page);
            }

            newDocument.save("/Users/sahilkapoor/downloads/with_toc_correctedV26.pdf");

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (originalDocument != null) {
                    originalDocument.close();
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
