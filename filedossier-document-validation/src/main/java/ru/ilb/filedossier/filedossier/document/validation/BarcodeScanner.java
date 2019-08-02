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

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ru.ilb.filedossier.functions.MapRuntimeFunction;

/**
 * BarcodeScanner gets the bar codes from each page of PDF file specified in the request. This class
 * is a wrapper over python script, call it by MapRuntimeFunction with request arguments and
 * receives founded bar codes in JSON format.
 * <p>
 * BarcodeScanner request is a JSON structure in the form of {@code Map<String, Object>}. It's
 * contains path of source PDF document, bar code search area, and path of directory, where pages
 * will be saved.
 * <p>
 * Bar code format example:
 * <p>
 * <i> doctree:11f462ebdb14a5673ff41a5c75c5176552fad343:1:5 </i>
 * <p>
 * TODO: return one bar code
 *
 * @see ru.ilb.filedossier.functions.MapRuntimeFunction
 * @author kuznetsov_me
 */
public class BarcodeScanner {

    /**
     * URI of the bar code scanner script.
     */
    private URI commandUri;

    /**
     * Request data for the script in the form of {@code Map<String, Object>}. Such a structure is
     * easy to convert to/from JSON.
     */
    Map<String, Object> requestMap;

    private BarcodeScanner() {
    }

    /**
     * @return list with bar codes of each page that PDF document contains.
     */
    public Map<String, String> recognizeBarcodeData() {

        if (commandUri == null) {
            throw new IllegalStateException("Command URI is not set");
        }
        return executeCommand();
    }

    private Map<String, String> executeCommand() {

        List<Map<String, Object>> responseObjects = (LinkedList) new MapRuntimeFunction(commandUri)
                .apply(requestMap)
                .get("pages");

        Map<String, String> barcodeDatas = new HashMap<>();

        responseObjects.stream().forEach((responseObject) -> {

            String barcodeKey = (String) responseObject.get("file");
            String barcode = (String) responseObject.get("barcode");

            barcodeDatas.put(barcodeKey, barcode);
        });

        return barcodeDatas;
    }

    public static RequestBuilder newRequestBuilder() {
        return new BarcodeScanner().new RequestBuilder();
    }

    /**
     * RequestBuilder builds the request map from the specified arguments.
     */
    public class RequestBuilder {

        /**
         * Original PDF document URI.
         */
        private String documentPath;

        /**
         * Bar code search area.
         */
        private DocumentArea barcodeArea;

        private RequestBuilder() {
        }

        public RequestBuilder withDocument(URI documentUri) {
            this.documentPath = documentUri.getPath();
            return this;
        }

        public RequestBuilder toCommand(URI commandUri) {
            BarcodeScanner.this.commandUri = commandUri;
            return this;
        }

        public RequestBuilder withArea(float x, float y, float h, float w) {
            DocumentArea area = new DocumentArea(x, y, h, w);
            this.barcodeArea = area;
            return this;
        }

        /**
         * Builds BarcodeScanner with request arguments map.
         */
        public BarcodeScanner build() {
            try {
                BarcodeScanner.this.requestMap = new HashMap<>();

                Map<String, Object> barcode = new HashMap<>();
                barcode.put("area", Optional.of(barcodeArea.asMap()).orElseThrow(
                            NullPointerException::new));

                Map<String, Object> source = new HashMap<>();
                source.put("document", Optional.of(documentPath).orElseThrow(
                           NullPointerException::new));

                source.put("barcode", Optional.of(barcode).orElseThrow(NullPointerException::new));

                requestMap.put("source", Optional.of(source).orElseThrow(NullPointerException::new));

                return BarcodeScanner.this;

            } catch (NullPointerException e) {
                throw new IllegalStateException("One of the request arguments is not set: " + e);
            }
        }
    }
}
