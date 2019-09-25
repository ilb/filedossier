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

import com.drew.imaging.ImageProcessingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ru.ilb.filedossier.barcode.entities.Barcode;
import ru.ilb.filedossier.metadata.extractor.ImageUtils;

/**
 *
 * @author kuznetsov_me
 */
public class ScanXMPMetaProvider {

    byte[] rawImage;

    public ScanXMPMetaProvider(byte[] rawImage) {
        this.rawImage = rawImage;
    }

    public Barcode getBarcode() throws IOException {

        String barcode;
        try {
            barcode = ImageUtils.extractXMPMetadata(rawImage, "barcode");
        } catch (ImageProcessingException e) {
            throw new RuntimeException("Error extracting metadata: " + e);
        }

        if (barcode != null) {
            return new Barcode(barcode);
        } else {
            return null;
        }
    }

    public List<DocumentArea> getSignatureCoordinates() throws IOException {
        try {

            List<DocumentArea> signatureCoordinates = new ArrayList<>();

            String signatures = ImageUtils.extractXMPMetadata(rawImage, "signatures");
            String[] signaturesArray = signatures.split(";");

            for (String coordinates : signaturesArray) {
                String[] coordinatesArray = coordinates.split(":");
                signatureCoordinates.add(new DocumentArea(Float.valueOf(coordinatesArray[0]),
                        Float.valueOf(coordinatesArray[1]),
                        Float.valueOf(coordinatesArray[2]),
                        Float.valueOf(coordinatesArray[3])));
            }
            return signatureCoordinates;

        } catch (ImageProcessingException | NullPointerException e) {
            return null;
        }
    }

}
