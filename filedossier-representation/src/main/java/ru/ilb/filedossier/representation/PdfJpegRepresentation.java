package ru.ilb.filedossier.representation;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfJpegRepresentation extends IdentityRepresentation {

    public PdfJpegRepresentation() {
        super("image/jpeg");
    }

    @Override
    public byte[] getContents() throws IOException {
        try (PDDocument pdfDocument = PDDocument.load(parent.getContents()); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            if (!isSinglePage()) {
                return parent.getContents();
            }
            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            BufferedImage image = renderer.renderImageWithDPI(0, 300, ImageType.RGB);
            ImageIO.write(image, "jpg", outputStream);
            return outputStream.toByteArray();
        }
    }

    private boolean isSinglePage() {
        try (PDDocument pdfDocument = PDDocument.load(parent.getContents())){
            return pdfDocument.getNumberOfPages() == 1;
        } catch (IOException e) {
            throw new RuntimeException("Corrupted PDF document");
        }
    }

    @Override
    public String getExtension() {
        if (parent == null || parent.getParent() == null) {
            return ".jpeg";
        } else {
            return isSinglePage() ? ".jpeg" : parent.getExtension();
        }
    }

    @Override
    public String getMediaType() {
        if (parent == null || parent.getParent() == null) {
            return "image/jpeg";
        } else {
            return isSinglePage() ? "image/jpeg" : parent.getMediaType();
        }
    }
}
