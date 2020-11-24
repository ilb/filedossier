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
package ru.ilb.filedossier.metadata.extractor;

import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPMeta;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.xmp.XmpDirectory;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;

/**
 *
 * @author kuznetsov_me
 */
public final class ImageUtils {

    private ImageUtils() {
    }

    public static String extractXMPMetadata(byte[] rawImage, String propName) throws
            IOException, ImageProcessingException {

        if (!MimeTypeUtil.guessMimeTypeFromByteArray(rawImage).equals("image/jpeg")) {
            return null;
        }

        InputStream is = new BufferedInputStream(new ByteArrayInputStream(rawImage));

        Metadata metadata = ImageMetadataReader.readMetadata(is);

        XmpDirectory directory = metadata.getFirstDirectoryOfType(XmpDirectory.class);

        XMPMeta xmpMeta = directory.getXMPMeta();

        try {
            String propValue = xmpMeta.getPropertyString("http://ns.adobe.com/xap/1.0/",
                    "xmp:" + propName);
            return propValue;
        } catch (XMPException e) {
            throw new RuntimeException("XMP parse error: " + e);
        }
    }
}
