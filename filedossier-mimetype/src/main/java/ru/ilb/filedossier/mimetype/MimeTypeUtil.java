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

import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author slavb
 */
public class MimeTypeUtil {

    private static MimeTypeRepository mimeTypeRepository = new MimeTypeRepository();

    public static String getExtension(String mimeType) {
        Iterator<String> itr = mimeTypeRepository.getMimeTypes().getOrDefault(mimeType, Collections.<String>emptyList()).iterator();
        return itr.hasNext() ? itr.next() : null;
    }

}
