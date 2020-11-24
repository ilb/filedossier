package ru.ilb.filedossier.document.merger.functions.image;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.AreaBreakType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import ru.ilb.filedossier.document.merger.functions.DocumentMerger;

/**
 * Merges two images to PDF document.
 */
public class ImagesMerger implements DocumentMerger {

    @Override
    public byte[] apply(byte[] rawImage1, byte[] rawImage2) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(os);
            PdfDocument document = new PdfDocument(writer);

            Image image1 = buildImage(rawImage1);
            Image image2 = buildImage(rawImage2);

            Rectangle rectangle = calculateDefaultRectangle(image1, image2);
            Document documentRoot = new Document(document, new PageSize(rectangle));
            documentRoot.add(image1.scaleToFit(rectangle.getWidth(), rectangle.getHeight()))
                    .add(new AreaBreak(AreaBreakType.NEXT_PAGE))
                    .add(image2.scaleToFit(rectangle.getWidth(), rectangle.getHeight()));

            document.close();
            return os.toByteArray();
        } catch (IOException e) {
            throw new UnableToMergeDocuments(e);
        }
    }

    /**
     * Calculates biggest page size from specified images, to choose as default for all new pages.
     *
     * @return page size
     */
    private Rectangle calculateDefaultRectangle(Image image1, Image image2) {
        Comparator<Image> comparator = Comparator.comparing(image -> image.getImageWidth() * image.getImageHeight());
        if (comparator.compare(image1, image2) > 0) {
            return new Rectangle(
                    (int) image1.getImageWidth(),
                    (int) image1.getImageHeight());
        } else {
            return new Rectangle(
                    (int) image2.getImageWidth(),
                    (int) image2.getImageHeight());
        }
    }

    private Image buildImage(byte[] rawImage) {
        ImageData imageData = ImageDataFactory.create(rawImage);
        return new Image(imageData)
                .setFixedPosition(0, 0);
    }
}
