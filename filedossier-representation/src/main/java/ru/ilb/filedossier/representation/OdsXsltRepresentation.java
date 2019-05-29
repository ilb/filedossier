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
package ru.ilb.filedossier.representation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class OdsXsltRepresentation implements Representation {

    private final String mediaType;
    private final URI stylesheetUri;
    private final URI templateUri;

    public OdsXsltRepresentation(String mediaType, URI stylesheetUri, URI templateUri) {
        this.stylesheetUri = stylesheetUri;
        this.templateUri = templateUri;
        this.mediaType = mediaType;
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public byte[] processContent(byte[] source, String mediaType) throws IOException {
        Map<String, String> attributes = new HashMap<>();
        Path tempDir = Files.createTempDirectory(getClass().getSimpleName());
        Path templatePath = tempDir.resolve("template.ods");
        Files.copy(Paths.get(templateUri), templatePath);
        URI templateUriJar = URI.create("jar:" + templatePath.toUri().toString());
        byte[] result;
        try (FileSystem zipFileSys = FileSystems.newFileSystem(templateUriJar, attributes);) {
            Path path = zipFileSys.getPath("content.xml");
            byte[] content = Files.readAllBytes(path);
            byte[] stylesheet = Files.readAllBytes(Paths.get(stylesheetUri));
            try {
                content = processContent(tempDir, source, content, stylesheet);
            } catch (TransformerException ex) {
                throw new RuntimeException(ex);
            }
            Files.delete(path);
            Files.write(path, content);
            result = Files.readAllBytes(templatePath);
        } finally {
            // remove temp dir with contents solution from https://stackoverflow.com/a/42267494/3141736
            Files.walk(tempDir)
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);
        }
        return result;
    }

    private byte[] processContent(Path tempDir, byte[] source, byte[] content, byte[] stylesheet) throws IOException, TransformerException {
        //File.createTe

        Source aStyleSheetInputSource = new StreamSource(new InputStreamReader(new ByteArrayInputStream(stylesheet)));
        aStyleSheetInputSource.setSystemId(tempDir.resolve("content.xsl").toString());

        // save data file to temp dir
        Files.write(tempDir.resolve("data.xml"), source);

        Source aInputSource = new StreamSource(new InputStreamReader(new ByteArrayInputStream(content)));

        // Выходные данные
        File aOutputFile = tempDir.resolve("content.xml").toFile(); // File.createTempFile("tmp", ".xml",tempDirWithPrefix.toFile());
        Result aOutputResult = new StreamResult(aOutputFile);

        // Преобразование
        TransformerFactory aFactory = TransformerFactory.newInstance();
        Transformer aTransformer = aFactory.newTransformer(aStyleSheetInputSource);

        aTransformer.transform(aInputSource, aOutputResult);

        return Files.readAllBytes(aOutputFile.toPath());

    }

}
