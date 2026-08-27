package com.karthik.CareerPilot.AI.services;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfTextExtractorService {

    public String extractText(MultipartFile file)
            throws Exception {

        if (file.isEmpty()) {
            throw new RuntimeException(
                    "Resume file is empty"
            );
        }

        if (!file.getOriginalFilename()
                .toLowerCase()
                .endsWith(".pdf")) {

            throw new RuntimeException(
                    "Only PDF files are supported"
            );
        }

        try (PDDocument document =
                     Loader.loadPDF(file.getBytes())) {

            PDFTextStripper stripper =
                    new PDFTextStripper();

            return stripper.getText(document);
        }
    }
}