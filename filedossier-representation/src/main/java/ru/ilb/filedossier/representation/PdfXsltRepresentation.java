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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author kuznetsov_me
 */

public class PdfXsltRepresentation extends IdentityRepresentation {

    private final static String OUTPUT_FORMAT = "application/pdf";

    protected final URI stylesheetUri;
    protected final URI contentUri;
    private static FopFactory fopFactory = null;

    public PdfXsltRepresentation(String mediaType, URI stylesheetUri, URI contentUri) {
	super(mediaType);

	if (!mediaType.equals(OUTPUT_FORMAT)) {
	    throw new IllegalArgumentException("Unsupported media type: " + mediaType);
	}

	this.stylesheetUri = stylesheetUri;
	this.contentUri = contentUri;
    }

    @Override
    public byte[] getContents() {
	try {
	    return processContent(parent.getContents(), stylesheetUri, contentUri);
	} catch (IOException | TransformerException | URISyntaxException | FOPException ex) {
	    throw new RuntimeException(ex);
	}
    }

    private static byte[] processContent(byte[] source, URI stylesheetUri, URI contentUri)
	    throws IOException, TransformerException, URISyntaxException, FOPException {

	byte[] stylesheet = Files.readAllBytes(Paths.get(stylesheetUri));
	Source stylesheetSource = new StreamSource(new InputStreamReader(new ByteArrayInputStream(stylesheet)));
	stylesheetSource.setSystemId(stylesheetUri.toString());

	byte[] content = Files.readAllBytes(Paths.get(contentUri));
	Source contentSource = new StreamSource(new InputStreamReader(new ByteArrayInputStream(content)));

	TransformerFactory factory = TransformerFactory.newInstance();
	factory.setURIResolver(new URIResolverImpl());
	Transformer transformer = factory.newTransformer(stylesheetSource);
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	transformer.transform(contentSource, getStreamResult(out));
	return out.toByteArray();
    }

    private static Result getStreamResult(ByteArrayOutputStream out)
	    throws FOPException, TransformerException, IOException {

	if (fopFactory == null) {
	    try {
		fopFactory = FopFactory.newInstance(
			PdfXsltRepresentation.class.getClassLoader().getResource("fop/").toURI(),
			PdfXsltRepresentation.class.getResourceAsStream("/fop/fopconf.xml"));
	    } catch (URISyntaxException | SAXException ex) {
		throw new RuntimeException(ex);
	    }
	}
	FOUserAgent userAgent = fopFactory.newFOUserAgent();
	Fop fop = fopFactory.newFop(OUTPUT_FORMAT, userAgent, out);
	return new SAXResult(fop.getDefaultHandler());
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
