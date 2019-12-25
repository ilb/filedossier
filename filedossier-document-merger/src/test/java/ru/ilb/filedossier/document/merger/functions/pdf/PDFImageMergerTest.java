package ru.ilb.filedossier.document.merger.functions.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.junit.Test;
import ru.ilb.filedossier.document.merger.functions.DocumentMerger;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PDFImageMergerTest {

    @Test
    public void testMergeImageToPdf() throws IOException {
        int expectedNumberOfPages = 2;
        byte[] testDocument = getRawResource("pdfdocument.pdf");
        byte[] testImage = getRawResource("image.png");

        PDFImageMerger instance = new PDFImageMerger(DocumentMerger.MergeDirection.TO_END);
        byte[] mergedDocument = instance.apply(testDocument, testImage);

        try (InputStream is = new ByteArrayInputStream(mergedDocument)) {
            PdfReader reader = new PdfReader(is);
            PdfDocument document = new PdfDocument(reader);
            assertEquals(expectedNumberOfPages, document.getNumberOfPages());
        }
    }

    @Test
    public void testMergePdfToImage() throws IOException {
        int expectedNumberOfPages = 2;
        byte[] testDocument = getRawResource("pdfdocument.pdf");
        byte[] testImage = getRawResource("image.png");

        PDFImageMerger instance = new PDFImageMerger(DocumentMerger.MergeDirection.TO_START);
        byte[] mergedDocument = instance.apply(testImage, testDocument);

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
