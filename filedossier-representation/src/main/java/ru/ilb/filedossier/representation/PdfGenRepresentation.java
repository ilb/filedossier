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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import ru.ilb.filedossier.entities.DossierContents;
import ru.ilb.filedossier.entities.DossierPath;
import ru.ilb.filedossier.functions.WebResourceFunction;

/**
 * Dossier representation for XML to PDF using PdfGen
 *
 * @author kuznetsov_me
 */
public class PdfGenRepresentation extends IdentityRepresentation {

    private final static String OUTPUT_FORMAT = "application/pdf";

    // base uri, contains three %s format flags - for stylesheet, scheme and
    // meta uri's
    private final static String BASE_URI = "http://devel.net.ilb.ru:8080/pdfgen/fopservlet?xslt=%s&xsd=%s&metaUrl=%s";

    private final WebResourceFunction webResourceFunction;

    private byte[] content;

    public PdfGenRepresentation(String mediaType, URI stylesheetUri, URI schemeUri, URI metaUri)
            throws MalformedURLException {
        super(mediaType);

        URL resourceUrl = new URL(
                String.format(BASE_URI, stylesheetUri.toString(), schemeUri.toString(),
                        metaUri.toString()));
        webResourceFunction = new WebResourceFunction(resourceUrl);

        if (!mediaType.equals(OUTPUT_FORMAT)) {
            throw new IllegalArgumentException("Unsupported format: " + mediaType);
        }
    }

    // MVP realization
    @Override
    public byte[] getContents() {
        //byte[] document = webResourceFunction.apply(parent.getContents());

        byte[] result = null;

        InputStream representationStream = getClass().getClassLoader().getResourceAsStream(
                "representation.pdf");

        Path tmpFile;
        try {
            tmpFile = Files.createTempFile("representation", ".pdf");
            Files.copy(representationStream, tmpFile, StandardCopyOption.REPLACE_EXISTING);
            return Files.readAllBytes(tmpFile);
        } catch (IOException | NullPointerException ex) {
            return webResourceFunction.apply(parent.getContents());
        }
    }

    @Override
    public String getExtension() {
        return "pdf";
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public void setParent(DossierPath parent) {
        assert DossierContents.class.isAssignableFrom(
                parent.getClass()) : "DossierContents instance should be passed as argument instead of "
                + parent.getClass().getCanonicalName();

        this.parent = (DossierContents) parent;
    }
}
