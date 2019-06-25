/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ilb.filedossier.functions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 *
 * @author slavb
 */
public class WebResourceFunction implements ByteFunction {

    private static final String REQUEST_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/xml";

    private final URL resourceUrl;

    public WebResourceFunction(URI resourceUri) {
	try {
	    System.out.println(resourceUri);
	    this.resourceUrl = resourceUri.toURL();
	} catch (MalformedURLException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @Override
    public byte[] apply(byte[] template) {
	try {
	    HttpURLConnection httpConnection = (HttpURLConnection) resourceUrl.openConnection();
	    httpConnection.setRequestMethod(REQUEST_METHOD);
	    httpConnection.setRequestProperty("Content-Type", CONTENT_TYPE);
	    httpConnection.setDoOutput(true);

	    try (OutputStream outStream = httpConnection.getOutputStream();
		    OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream)) {
		outStreamWriter.write(new String(template));
		outStreamWriter.flush();
	    }

	    try (InputStream responseContent = httpConnection.getInputStream();
		    ByteArrayOutputStream out = new ByteArrayOutputStream()) {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = responseContent.read(buffer)) != -1) {
		    out.write(buffer, 0, len);
		}
		return out.toByteArray();
	    }

	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}

    }

}
