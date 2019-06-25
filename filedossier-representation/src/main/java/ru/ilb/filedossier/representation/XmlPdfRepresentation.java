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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import ru.ilb.filedossier.functions.WebResourceFunction;

/**
 *
 * @author kuznetsov_me
 */
public class XmlPdfRepresentation extends IdentityRepresentation {

    private final static String OUTPUT_FORMAT = "application/pdf";
    private final static URI BASE_URI = URI.create("http://devel.net.ilb.ru:8080/pdfgen/fopservlet");

    private final URI contentUri;
    private final URI resourceUri;

    public XmlPdfRepresentation(String mediaType, URI contentUri, URI resourceUri) {
	super(mediaType);

	if (!mediaType.equals(OUTPUT_FORMAT)) {
	    throw new IllegalArgumentException("Unsupported format: " + mediaType);
	}

	this.contentUri = contentUri;
	this.resourceUri = resourceUri;
    }

    @Override
    public byte[] getContents() {
	try {
	    byte[] content = Files.readAllBytes(Paths.get(contentUri));
	    return new WebResourceFunction(new URI(BASE_URI.toString() + resourceUri.toString())).apply(content);
	} catch (IOException | URISyntaxException ex) {
	    throw new RuntimeException(ex);
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
}
