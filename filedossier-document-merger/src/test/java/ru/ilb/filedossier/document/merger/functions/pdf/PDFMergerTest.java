package ru.ilb.filedossier.document.merger.functions.pdf;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class PDFMergerTest {

    @Test
    public void apply() throws IOException {
        int expectedNumberOfPages = 2;
        byte[] testPdf = getRawResource("pdfdocument.pdf");

        PDFMerger instance = new PDFMerger();
        byte[] mergedDocument = instance.apply(testPdf, testPdf);

        try (InputStream is = new ByteArrayInputStream(mergedDocument)) {
            PdfReader reader = new PdfReader(is);
            PdfDocument document = new PdfDocument(reader);
            assertEquals(expectedNumberOfPages, document.getNumberOfPages());
        }
    }

    private byte[] getRawResource(String name) throws IOException {
        String fileDest = String.valueOf(getClass()
                .getClassLoader()
                .getResource(name)).replace("file:", "");
        return Files.readAllBytes(Paths.get(fileDest));
    }
}
