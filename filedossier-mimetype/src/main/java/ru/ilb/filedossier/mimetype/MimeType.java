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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author slavb
 */
public class MimeType {

    //private static final URI MIME_TYPES_URI = URI.create("classpath:mimetype/mime.types");
    private static final Map<String, List<String>> MIME_TYPES;

    static {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(MimeType.class.getClassLoader().getResource("mimetype/mime.types").toURI()));
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(MimeType.class.getName()).log(Level.SEVERE, null, ex);
        }
        MIME_TYPES = lines.stream()
                .filter(l -> !l.startsWith("#"))
                .map(l -> l.split("\\s+"))
                .collect(Collectors.toMap(l -> l[0], l -> Arrays.asList(Arrays.copyOfRange(l, 1, l.length))));
    }

    public static String getExtension(String mimeType) {
        Iterator<String> itr = MIME_TYPES.getOrDefault(mimeType, Collections.<String>emptyList()).iterator();
        return itr.hasNext() ? itr.next() : null;
    }

}
