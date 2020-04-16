package ru.ilb.filedossier.representation;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PdfJpegRepresentationTest {

    @Test
    public void testGenerateRepresentation() throws IOException, URISyntaxException {
        byte[] pdfToRepresent = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("pages/pages/pdf-singlepage.pdf").toURI()));
        byte[] expectedResult = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("pages/pages/pdfpage.jpg").toURI()));
        DossierContentsHolder holder = new DossierContentsHolder(pdfToRepresent, "application/pdf", "pdf", "pdf", ".pdf");

        PdfJpegRepresentation pdfJpegRepresentation = new PdfJpegRepresentation();
        pdfJpegRepresentation.setParent(holder);

        assertEquals("image/jpeg", pdfJpegRepresentation.getMediaType());
        assertEquals(".jpeg", pdfJpegRepresentation.getExtension());
        assertArrayEquals(expectedResult, pdfJpegRepresentation.getContents());
    }

    @Test
    public void testGenerateRepresentationFromDoublePagePdf() throws IOException, URISyntaxException {
        byte[] pdfToRepresent = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("pages/pages/pdf-doublepage.pdf").toURI()));
        DossierContentsHolder holder = new DossierContentsHolder(pdfToRepresent, "application/pdf", "pdf", "pdf", ".pdf");
        DossierContentsHolder holderParent = new DossierContentsHolder(null, "application/pdf", "pdf", "pdf", ".pdf");
        holder.setParent(holderParent);

        PdfJpegRepresentation pdfJpegRepresentation = new PdfJpegRepresentation();
        pdfJpegRepresentation.setParent(holder);

        assertEquals("application/pdf", pdfJpegRepresentation.getMediaType());
        assertEquals(".pdf", pdfJpegRepresentation.getExtension());
        assertArrayEquals(pdfToRepresent, pdfJpegRepresentation.getContents());
    }
}
