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
package ru.ilb.filedossier.representation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author kuznetsov_me
 */
public class XmlPdfRepresentation extends IdentityRepresentation {
    
    private static final String PDFGEN_URL = "http://devel.net.ilb.ru:8080/pdfgen/fopservlet";
    
    protected final URI stylesheetUri;
    protected final URI contentUri;
    
    public XmlPdfRepresentation(String mediaType, URI stylesheetUri, URI contentUri) {
        super(mediaType);
        this.stylesheetUri = stylesheetUri;
        this.contentUri = contentUri;
    }
    
    @Override
    public byte[] getContents() {
        try {
            return processContent(parent.getContents(), stylesheetUri, contentUri);
        } catch (IOException | TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static byte[] processContent(byte[] source, URI stylesheetUri, URI contentUri) throws IOException, TransformerException {
        
        Path tempDir = Files.createTempDirectory("XmlPdfRepresentation");
        
        byte[] content = Files.readAllBytes(Paths.get(contentUri));
        byte[] stylesheet = Files.readAllBytes(Paths.get(stylesheetUri));
        Source aStyleSheetInputSource = new StreamSource(new InputStreamReader(new ByteArrayInputStream(stylesheet)));
        aStyleSheetInputSource.setSystemId(tempDir.resolve("projectteam2fo.xsl").toString());
        Files.write(tempDir.resolve("data.xml"), source);

        Source aInputSource = new StreamSource(new InputStreamReader(new ByteArrayInputStream(content)));
        File aOutputFile = tempDir.resolve("projectteam.fo").toFile();
        Result aOutputResult = new StreamResult(aOutputFile);
        TransformerFactory aFactory = TransformerFactory.newInstance();
        Transformer aTransformer = aFactory.newTransformer(aStyleSheetInputSource);
        aTransformer.transform(aInputSource, aOutputResult);
        
        byte[] result;
        try {
             result = requestPdfContent(aOutputFile);
        } finally {
            Files.walk(tempDir)
                .map(Path::toFile)
                .sorted((o1, o2) -> -o1.compareTo(o2))
                .forEach(File::delete);
        }
        return result;
    }
    
    public static byte[] requestPdfContent(File foFile) throws FileNotFoundException, IOException {
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(PDFGEN_URL);
        
        InputStreamEntity foEntity = new InputStreamEntity(new FileInputStream(foFile));
        foEntity.setContentType("application/xml");
        foEntity.setChunked(true);
        request.setEntity(foEntity);
        
        HttpResponse response = client.execute(request);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        response.getEntity().writeTo(stream);
        return stream.toByteArray();
    }
    
    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String getExtension() {
        return "pdf";
    }
    
}
