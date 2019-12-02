package ru.ilb.filedossier.document.merger.functions.pdf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import ru.ilb.filedossier.document.merger.functions.DocumentMerger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Merges PDF document and image, i.e. adds image to PDF.
 */
public class PDFImageMerger implements DocumentMerger {

    private DocumentMerger.MergeDirection direction;

    /**
     * Basic constructor, creates pdf/image merger with merge direction.
     * @see ru.ilb.filedossier.document.merger.functions.DocumentMerger.MergeDirection
     */
    public PDFImageMerger(MergeDirection direction) {
        this.direction = direction;
    }

    @Override
    public byte[] apply(byte[] pdf, byte[] image) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
            InputStream is = new ByteArrayInputStream(pdf)) {

            PdfReader reader = new PdfReader(is);
            PdfWriter writer = new PdfWriter(os);
            PdfDocument document = new PdfDocument(reader, writer);

            putImageToPDF(document, image, direction);
            document.close();
            return os.toByteArray();
        } catch (IOException e) {
            throw new UnableToMergeDocuments(e);
        }
    }

    private void putImageToPDF(PdfDocument document, byte[] rawImage, MergeDirection direction) {
        Rectangle documentBox = document.getFirstPage().getPageSize();
        ImageData imageData = ImageDataFactory.create(rawImage);
        PdfPage newPage = null;

        switch (direction) {
            case TO_START: newPage = document.addNewPage(1, new PageSize(documentBox));
                break;
            case TO_END: newPage = document.addNewPage(new PageSize(documentBox));
        }

        AffineTransform at = AffineTransform.getTranslateInstance(
                documentBox.getX(),
                documentBox.getY());

        at.concatenate(AffineTransform.getScaleInstance(
                imageData.getWidth(),
                imageData.getHeight()));

        PdfCanvas canvas = new PdfCanvas(newPage);
        canvas.addImage(imageData, documentBox, true);
    }
}
