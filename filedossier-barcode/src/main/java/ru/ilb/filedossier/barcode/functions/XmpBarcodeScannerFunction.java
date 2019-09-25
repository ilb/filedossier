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
package ru.ilb.filedossier.barcode.functions;

import com.drew.imaging.ImageProcessingException;
import java.io.IOException;
import java.util.Optional;
import ru.ilb.filedossier.barcode.entities.Barcode;
import ru.ilb.filedossier.metadata.extractor.ImageUtils;

public class XmpBarcodeScannerFunction implements BarcodeScannerFunction {

    @Override
    public Optional<Barcode> apply(byte[] t) {
        String barcode = null;
        try {
            barcode = ImageUtils.extractXMPMetadata(t, "barcode");
        } catch (IOException e) {
            throw new RuntimeException("Error getting image to scan" + e);
        } catch (ImageProcessingException e) {
            throw new RuntimeException("Error while barcode scanning" + e);
        }

        if (barcode == null) {
            return Optional.empty();
        }
        return Optional.of(new Barcode(barcode));
    }
}
