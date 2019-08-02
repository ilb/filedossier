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
package ru.ilb.filedossier.utils;

import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPIterator;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.xmp.XmpDirectory;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;

/**
 *
 * @author kuznetsov_me
 */
public class ImageUtils {

    public static Map<String, String> extractXMPMetadata(byte[] rawImage) throws IOException,
                                                                                 ImageProcessingException {
        if (MimeTypeUtil.guessMimeTypeFromByteArray(rawImage) != "image/jpeg") {
            return null;
        }

        InputStream is = new BufferedInputStream(new ByteArrayInputStream(rawImage));

        Metadata metadata = ImageMetadataReader.readMetadata(is);

        Collection<XmpDirectory> xmpDirectories = metadata.getDirectoriesOfType(XmpDirectory.class);

        Map<String, String> metadataMap = new HashMap<>();

        for (XmpDirectory directory : xmpDirectories) {
            XMPMeta xmpMeta = directory.getXMPMeta();

            try {
                XMPIterator iterator = xmpMeta.iterator();

                while (iterator.hasNext()) {
                    XMPPropertyInfo xmpPropertyInfo = (XMPPropertyInfo) iterator.next();

                    String metadataName = xmpPropertyInfo.getPath();
                    String metadataValue = xmpPropertyInfo.getValue();

                    if (metadataName != null) {
                        // metadata path has the 'xmp:' prefix, we don't need it
                        metadataName = metadataName.replace("xmp:", "");
                        metadataMap.put(metadataName, metadataValue);
                    }
                }

            } catch (XMPException e) {
                throw new RuntimeException("XMP parse error: " + e);
            }
        }
        return metadataMap;
    }
}
