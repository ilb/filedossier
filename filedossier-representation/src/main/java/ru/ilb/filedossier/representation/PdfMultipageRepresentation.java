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
package ru.ilb.filedossier.representation;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.UnitValue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import ru.ilb.filedossier.entities.Store;

/**
 *
 * @author kuznetsov_me
 */
public class PdfMultipageRepresentation extends IdentityRepresentation {

    // Change to Path
    private Store store;

    public PdfMultipageRepresentation(String mediaType, Store store) {
        super(mediaType);
        this.store = store;
    }

    @Override
    public byte[] getContents() {
        try {
            List<byte[]> pages = store.getAllContents();
            return mergePages(pages);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private byte[] mergePages(List<byte[]> byteImages) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out));

        Document document = new Document(pdfDocument);

        int numberOfPages = byteImages.size();

        int pageNumber = 1;

        for (byte[] image : byteImages) {

            Image pageToInsert = new Image(ImageDataFactory.create(image))
                    .setAutoScale(true)
                    .setWidth(UnitValue.createPercentValue(100));

            document.add(pageToInsert);

            if (pageNumber != numberOfPages) {
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }

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

    @Override
    public String getExtension() {
        return "pdf";
    }

    @Override
    public String getMediaType() {
        return "application/pdf";
    }
}
