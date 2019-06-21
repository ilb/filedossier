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

    private final URL resourceUrl;
    private String requestMethod = "POST";
    private String contentType = "application/xml";

    public WebResourceFunction(URI resourceUrl) {
        try {
            this.resourceUrl = resourceUrl.toURL();
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public byte[] apply(byte[] t) {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) resourceUrl.openConnection();
            httpConnection.setRequestMethod(requestMethod);
            httpConnection.setRequestProperty("Content-Type", contentType);
            httpConnection.setDoOutput(true);

            try (OutputStream outStream = httpConnection.getOutputStream();
                    OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream)) {
                outStreamWriter.write(new String(t));
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

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

}
