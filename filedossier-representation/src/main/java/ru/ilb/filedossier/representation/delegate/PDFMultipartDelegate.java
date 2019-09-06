/*
 * Copyright 2019 kuznetsov_me.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.ilb.filedossier.representation.delegate;

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
import java.util.List;
import ru.ilb.filedossier.entities.RepresentationPart;
import ru.ilb.filedossier.entities.Store;

/**
 * Delegate work with multiple images, combines all of it within one PDF file.
 * @author kuznetsov_me
 */
public class PDFMultipartDelegate implements MultipartDelegate {

    private Store store;

    /**
     * @param store custom store, specified by representation
     */
    public PDFMultipartDelegate(Store store) {
        this.store = store;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRepresentationPart(RepresentationPart part) {
        try {
            store.setContents(part.getCode(), part.getContents());
        } catch (IOException e) {
            throw new RuntimeException("Error while saving representation part: " + e);
        }
    }

    /**
     * {@inheritDoc}
     * @return PDF document with all scans
     */
    @Override
    public byte[] getContents() {
        List<byte[]> byteImages;
        byteImages = store.getAllContents();
        try {
            return combineImages(byteImages);
        } catch (IOException e) {
            throw new RuntimeException("Error while combining representation parts: " + e);
        }
    }

    /**
     * Combine scan images from multi part store within one PDF file.
     * @param byteImages all scans from store
     * @return PDF document in byte array
     * @throws IOException
     */
    private byte[] combineImages(List<byte[]> byteImages) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out));

        int numberOfPages = byteImages.size();
        int pageNumber = 1;

        Document document = null;

        for (byte[] image : byteImages) {
            document = insertImageToPdf(pageNumber, numberOfPages, image, document, pdfDocument);
            pageNumber++;
        }

        document.close();
        pdfDocument.close();

        byte[] result;
        try (ByteArrayOutputStream resultStream = out) {
            result = resultStream.toByteArray();
        }
        return result;
    }

    private Document insertImageToPdf(int pageNumber, int numberOfPages, byte[] image,
            Document document, PdfDocument pdfDocument) {
        Rectangle rect = new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight());

        Image pageToInsert = new Image(ImageDataFactory.create(image))
                .scaleToFit(rect.getWidth(), rect.getHeight());

        pageToInsert.setFixedPosition(0, 0);

        PageSize pageSize = new PageSize(rect);

        if (document == null) {
            document = new Document(pdfDocument, pageSize);
        }
        document.add(pageToInsert);

        if (pageNumber != numberOfPages) {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        }

        return document;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        List<byte[]> parts = store.getAllContents();
        return parts.isEmpty();
    }
}
