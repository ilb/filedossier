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
package ru.ilb.filedossier.filedossier.document.validation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

/**
 * Class for inserting and extracting a meta data to specified PDF document.
 *
 * @author kuznetsov_me
 */
public class PdfMetadataManager {

    public static Map<String, String> extractMetadata(byte[] document) throws IOException {

        PDDocument pdfDocument = PDDocument.load(document);
        System.out.println(pdfDocument.getDocumentInformation().getMetadataKeys());
        PDDocumentInformation info = pdfDocument.getDocumentInformation();

        pdfDocument.close();

        Map<String, String> metadata = new HashMap<>();

        Set<String> keys = info.getMetadataKeys();
        keys.forEach(key -> metadata.put(key, info.getCustomMetadataValue(key)));

        return metadata;
    }

    public static byte[] insertMetadata(byte[] document, Map<String, String> metadata) throws
            IOException {

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
    }
}
