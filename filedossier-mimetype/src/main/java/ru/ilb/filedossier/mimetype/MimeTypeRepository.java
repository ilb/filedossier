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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author slavb
 */
public class MimeTypeRepository {

    //private static final URI MIME_TYPES_URI = URI.create("classpath:mimetype/mime.types");
    private final Map<String, List<String>> mimeTypes;

    MimeTypeRepository() {
        List<String> lines = new ArrayList<>();
        try {
            //lines = Files.readAllLines(Paths.get(MimeTypeUtil.class.getClassLoader().getResource("mimetype/mime.types").toURI()));
            lines = Files.readAllLines(Paths.get("/etc/mime.types"));
        } catch (IOException ex) { //URISyntaxException
            throw new RuntimeException(ex);
        }
        mimeTypes = lines.stream()
                .filter(l -> !l.startsWith("#"))
                .filter(l->(!l.isEmpty()))
                .map(l -> l.split("\\s+"))
                .collect(Collectors.toMap(l -> l[0], l -> Arrays.asList(Arrays.copyOfRange(l, 1, l.length))));
    }

    public Map<String, List<String>> getMimeTypes() {
        return mimeTypes;
    }
}
