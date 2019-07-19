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
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;
import ru.ilb.filedossier.functions.BadCommandResponseException;

/**
 *
 * @author kuznetsov_me
 */
public class BarcodeScannerTest {

    URI documentUri;

    URI commandUri;

    public BarcodeScannerTest() throws URISyntaxException {
        documentUri = getClass().getClassLoader().getResource("documentbarcode/example.pdf").toURI();
        commandUri = getClass().getClassLoader().getResource("documentbarcode/example.sh").toURI();
    }

    /**
     * Test of getBarcodeData method, of class BarcodeScanner.
     */
    @Test
    public void testGetBarcodeData() {
        System.out.println("getBarcodeData");
        String firstExpectedBarcode = null;
        String secondExpectedBarcode = "doctree:11f462ebdb14a5673ff41a5c75c5176552fad343:1:5";

        BarcodeScanner instance = BarcodeScanner.newRequestBuilder()
                .withDocument(documentUri)
                .withArea(34, 95, 51, 97.5f)
                .toCommand(commandUri)
                .build();
        List<Map<String, Object>> result = instance.getBarcodeData();

        String firstResultBarcode = (String) result.get(0).get("barcode");
        assertEquals(firstExpectedBarcode, firstResultBarcode);

        String secondResultBarcode = (String) result.get(1).get("barcode");
        assertEquals(secondExpectedBarcode, secondResultBarcode);
    }

    /**
     * Test of building BarcodeScanner without arguments
     *
     * @exception IllegalStateException
     */
    @Test(expected = IllegalStateException.class)
    public void testEmptyArguments() {
        System.out.println("emptyArguments");
        BarcodeScanner instance = BarcodeScanner.newRequestBuilder().build();
    }

    @Test(expected = BadCommandResponseException.class)
    public void testIncorrectArguments() {
        System.out.println("incorrectArguments");
        BarcodeScanner instance = BarcodeScanner.newRequestBuilder()
                .withDocument(documentUri)
                .withArea(34, 95, 51, 95)
                .toCommand(commandUri)
                .build();
        instance.getBarcodeData();
    }
}
