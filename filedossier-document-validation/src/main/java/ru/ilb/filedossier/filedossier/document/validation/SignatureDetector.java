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
package ru.ilb.filedossier.filedossier.document.validation;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import ru.ilb.filedossier.functions.MapRuntimeFunction;

/**
 * SignatureDetector checks for signatures each page of PDF document by comparing original PDF page with image scan. This class is a wrapper over python script, call it by
 * MapRuntimeFunction with request arguments and receives is exist value for each signature in JSON format.
 * <p>
 * SignatureDetector request is a JSON structure in the form of {@code Map<String, Object>}. It's contains path of original page, path of scan page and set of areas, where
 * signatures should be detected.
 *
 * @see ru.ilb.filedossier.functions.MapRuntimeFunction
 * @author kuznetsov_me
 */
public class SignatureDetector {

    /**
     * URI of the signature detector script.
     */
    private URI signatureDetectorUri;

    /**
     * Request data for the script in the form of {@code Map<String, Object>}. Such a structure is easy to convert to/from JSON.
     */
    private Map<String, Object> requestMap;

    private SignatureDetector() throws NamingException {
        InitialContext context = new InitialContext();
        signatureDetectorUri = URI.create((String) context.lookup(
                "java:comp/env/ru.bystrobank.apps.documentsignaturedetector.uri"));
    }

    /**
     * @return List of "is detected" boolean values of each signature areas.
     */
    public List<Boolean> detectSignatures() {
        if (signatureDetectorUri == null) {
            throw new IllegalStateException("Command URI not specified");
        }
        List<Map<String, Object>> signatures = (LinkedList) new MapRuntimeFunction(
                signatureDetectorUri)
                .apply(requestMap)
                .get("signatures");

        return signatures.stream()
                .map(signature -> (boolean) signature.get("detected"))
                .collect(Collectors.toList());
    }

    public static RequestBuilder newRequestBuilder() {
        try {
            return new SignatureDetector().new RequestBuilder();
        } catch (NamingException e) {
            throw new RuntimeException("Bad document signature detector URI: " + e);
        }
    }

    /**
     * Builds SignatureDetector with request arguments map.
     */
    public class RequestBuilder {

        /**
         * Original PDF page path
         */
        private String pdfPath;

        private String scanPath;

        private List<DocumentArea> signatureAreas;

        private RequestBuilder() {
            signatureAreas = new ArrayList<>();
        }

        public RequestBuilder withPDFPage(byte[] pdfPage) throws IOException {
            Path tempFile = Files.createTempFile("pdfpage", "jpg");
            Files.write(tempFile, pdfPage);
            this.pdfPath = tempFile.toString();
            return this;
        }

        public RequestBuilder withScanPage(byte[] scanPage) throws IOException {
            Path tempFile = Files.createTempFile("scanpage", "jpg");
            Files.write(tempFile, scanPage);
            this.scanPath = tempFile.toString();
            return this;
        }

        public RequestBuilder withSignatureAreas(List<DocumentArea> signatureAreas) {
            this.signatureAreas = signatureAreas;
            return this;
        }

        public RequestBuilder toCommand(URI commandUri) {
            SignatureDetector.this.signatureDetectorUri = commandUri;
            return this;
        }

        /**
         * Converts specified values into Map structure.
         */
        private Map<String, Object> createRequestMap() {
            try {
                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put("signatures", signatureAreas.stream()
                        .map(signatureArea -> signatureArea.asMap())
                        .collect(Collectors.toList()));
                requestMap.put("pdf_page", Optional.of(pdfPath)
                        .orElseThrow(NullPointerException::new));
                requestMap.put("scan_page", Optional.of(scanPath)
                        .orElseThrow(NullPointerException::new));
                return requestMap;
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("One of the arguments isn't set: " + e);
            }
        }

        public SignatureDetector build() {
            SignatureDetector.this.requestMap = createRequestMap();
            return SignatureDetector.this;
        }
    }
}
