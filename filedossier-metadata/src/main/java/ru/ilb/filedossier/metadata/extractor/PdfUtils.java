package ru.ilb.filedossier.metadata.extractor;

/*
 * Copyright 2019 kuznetsov_me.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * Util class for actions with PDF document.
 *
 * @author kuznetsov_me
 */
public class PdfUtils {

    public static Map<String, String> extractMetadata(byte[] document) {

        try {
            PDDocument pdfDocument = PDDocument.load(document);
            PDDocumentInformation info = pdfDocument.getDocumentInformation();

            pdfDocument.close();

            Map<String, String> metadata = new HashMap<>();

            Set<String> keys = info.getMetadataKeys();
            keys.forEach(key -> metadata.put(key, info.getCustomMetadataValue(key)));

            return metadata;
        } catch (IOException ex) {
            throw new RuntimeException("Bad document: " + ex);
        }
    }

    public static byte[] insertMetadata(byte[] document, Map<String, String> metadata) {

        try {
            PDDocument pdfDocument = PDDocument.load(document);
            PDDocumentInformation info = new PDDocumentInformation();
            metadata.forEach((key, value) -> info.setCustomMetadataValue(key, value));

            pdfDocument.setDocumentInformation(info);

            byte[] docWithMetadata;

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                pdfDocument.save(out);
                docWithMetadata = out.toByteArray();
            }

            pdfDocument.close();

            return docWithMetadata;
        } catch (IOException ex) {
            throw new RuntimeException("Bad document: " + ex);
        }
    }

    public static int getNumberOfPages(byte[] document) {
        try {
            PDDocument pdfDocument = PDDocument.load(document);
            return pdfDocument.getNumberOfPages();
        } catch (IOException ex) {
            throw new RuntimeException("Bad document: " + ex);
        }
    }

    public static byte[] extractPdfPage(byte[] document, int pageNum) {
        pageNum--;
        try (PDDocument pdfDocument = PDDocument.load(document);
                PDDocument pdfPage = new PDDocument()) {

            pdfPage.addPage(pdfDocument.getPage(pageNum));

            PDFRenderer renderer = new PDFRenderer(pdfPage);
            BufferedImage image = renderer.renderImageWithDPI(0, 100);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
                ImageIO.write(image, "jpg", out);
                return out.toByteArray();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Bad document: " + ex);
        }
    }
}
