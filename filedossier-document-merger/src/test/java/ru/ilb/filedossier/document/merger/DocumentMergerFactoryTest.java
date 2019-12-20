package ru.ilb.filedossier.document.merger;

import ru.ilb.filedossier.document.merger.functions.DocumentMerger;
import ru.ilb.filedossier.document.merger.functions.pdf.PDFImageMerger;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentMergerFactoryTest {

    @Test
    public void getDocumentMerger() {
        String mt1 = "application/pdf";
        String mt2 = "image/png";

        DocumentMergerFactory instance = DocumentMergerFactory.getInstance();
        DocumentMerger actualResult = instance.getDocumentMerger(mt1, mt2);
        assertTrue(actualResult instanceof PDFImageMerger);
    }

    @Test(expected = DocumentMergerFactory.UnsupportedMediaTypes.class)
    public void throwExceptionWhenNotSupported() {
        String mt1 = "image/png";
        String mt2 = "application/json";

        DocumentMergerFactory instance = DocumentMergerFactory.getInstance();
        DocumentMerger merger = instance.getDocumentMerger(mt1, mt2);
    }
}
