/*
 * Copyright 2019 slavb.
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
package ru.ilb.filedossier.mimetype;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author slavb
 */
public class MimeTypeUtil {

    private static MimeTypeRepository mimeTypeRepository = new MimeTypeRepository();

    public static String getExtension(String mimeType) {
        Iterator<String> itr = mimeTypeRepository
                .getMimeTypes().getOrDefault(mimeType, Collections.<String>emptyList()).iterator();
        return itr.hasNext() ? itr.next() : null;
    }

    public static String guessMimeTypeFromByteArray(byte[] rawFile) throws IOException {
        InputStream is = new BufferedInputStream(new ByteArrayInputStream(rawFile));
        return guessMimeType(is);
    }

    public static String guessMimeTypeFromFile(File file) throws FileNotFoundException, IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        return guessMimeType(is);
    }

    private static String guessMimeType(InputStream is) throws IOException {
        String mimeType = URLConnection.guessContentTypeFromStream(is);
        return mimeType;
    }

}
