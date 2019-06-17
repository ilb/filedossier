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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author kuznetsov_me
 */
public class XmlPdfRepresentation extends IdentityRepresentation {

    private final static String OUTPUT_FORMAT = "application/pdf";

    // style
    private final static String PDFGEN_URL = "http://devel.net.ilb.ru:8080/pdfgen/fopservlet?xslt="
	    + "https://devel.net.ilb.ru/meta/stylesheets/doctemplates/primary/loantreaty/credits/auto/V20140707.xsl&xsd="
	    + "https://devel.net.ilb.ru/meta/schemas/doctemplates/primary/loantreaty/credits/auto/V20140707.xsd&metaUrl="
	    + "https://devel.net.ilb.ru/meta/&uid=doctree:11f462ebdb14a5673ff41a5c75c5176552fad343:";

    protected final URI contentUri;

    public XmlPdfRepresentation(String mediaType, URI contentUri) {
	super(mediaType);

	if (!mediaType.equals(OUTPUT_FORMAT)) {
	    throw new IllegalArgumentException("Unsupported format: " + mediaType);
	}

	this.contentUri = contentUri;
    }

    @Override
    public byte[] getContents() {
	try {
	    byte[] content = Files.readAllBytes(Paths.get(contentUri));
	    return requestPdfGen(content);
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    private static byte[] requestPdfGen(byte[] content) throws MalformedURLException, IOException {
	URL pdfgenUrl = new URL(PDFGEN_URL);
	HttpURLConnection httpConnection = (HttpURLConnection) pdfgenUrl.openConnection();
	httpConnection.setRequestMethod("POST");
	httpConnection.setRequestProperty("Content-Type", "application/xml");
	httpConnection.setDoOutput(true);

	try (OutputStream outStream = httpConnection.getOutputStream();
		OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream)) {
	    outStreamWriter.write(new String(content));
	    outStreamWriter.flush();
	}

	try (InputStream pdf = httpConnection.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	    byte[] buffer = new byte[1024];
	    int len;
	    while ((len = pdf.read(buffer)) != -1) {
		out.write(buffer, 0, len);
	    }
	    return out.toByteArray();
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
