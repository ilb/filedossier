/*
 * Copyright 2020 slavb.
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
package ru.ilb.filedossier.filesystems.pdf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author slavb
 */
public class PdfFileSystemProviderTest {

    @Test
    public void testUri() throws URISyntaxException, IOException {

        URI fileUri = this.getClass().getResource("test.pdf").toURI();
        URI fsUri = URI.create("pdffs:" + fileUri.toString());
        System.out.println(fsUri);

        Map<String, String> attributes = new HashMap<>();
        Path contentsPath = null;
        try (FileSystem fs = FileSystems.newFileSystem(fsUri, attributes);) {
            Path path = fs.getPath("page-1.jpg");
            byte[] content = Files.readAllBytes(path);
            assertEquals(227037, content.length);
            contentsPath = ((PdfFileSystem) fs).getContentsPath();
        }
        assertFalse("Directory still exists", Files.exists(contentsPath));

    }
}
