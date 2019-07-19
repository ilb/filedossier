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
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.ilb.filedossier.functions.BadCommandResponseException;

/** *
 * @author kuznetsov_me
 */
public class SignatureDetectorTest {

    URI pageUri;

    URI scanUri;

    URI commandUri;

    public SignatureDetectorTest() throws URISyntaxException {
        pageUri = getClass().getClassLoader().getResource("signaturedetector/original.png").toURI();
        scanUri = getClass().getClassLoader().getResource("signaturedetector/scan.png").toURI();
        commandUri = getClass().getClassLoader().getResource("signaturedetector/example.sh").toURI();
    }

    @Test
    public void testDetectSignatures() {
        System.out.println("getBarcodeData");

        List<Boolean> expectedList = new LinkedList<>();
        expectedList.add(false);
        expectedList.add(true);

        SignatureDetector instance = SignatureDetector.newRequestBuilder()
                .withPDFPage(pageUri)
                .withScanPage(scanUri)
                .withSignatureArea(0, 0, 10, 10)
                .withSignatureArea(20, 117, 101, 127)
                .toCommand(commandUri)
                .build();
        List<Boolean> result = instance.detectSignatures();

        assertEquals(expectedList, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArguments() {
        System.out.println("emptyRequest");
        SignatureDetector instance = SignatureDetector.newRequestBuilder().build();
    }

    @Test(expected = BadCommandResponseException.class)
    public void testIncorrectArguments() {
        System.out.println("incorrectArguments");
        SignatureDetector instance = SignatureDetector
                .newRequestBuilder()
                .withPDFPage(commandUri)
                .withScanPage(scanUri)
                .withSignatureArea(0, 0, 10, 10)
                .withSignatureArea(0, 0, 0, 0)
                .toCommand(commandUri)
                .build();
        System.out.println(instance.detectSignatures());
    }

}
