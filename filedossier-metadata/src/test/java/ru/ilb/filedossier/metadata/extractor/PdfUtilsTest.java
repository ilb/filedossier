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
package ru.ilb.filedossier.metadata.extractor;

import ru.ilb.filedossier.metadata.extractor.PdfUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author kuznetsov_me
 */
public class PdfUtilsTest {

    byte[] pdfDocument;

    public PdfUtilsTest() throws IOException, URISyntaxException {
        pdfDocument = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource(
                "example.pdf").toURI()));
    }

    /**
     * Test of insertMetadata method, of class PdfUtils.
     */
    @Test
    public void testInsertMetadata() {
        System.out.println("insertMetadata");

        Map<String, String> metadata = new HashMap<>();

        metadata.put("testkey1", "testvalue1");
        metadata.put("testkey2", "testvalue2");

        pdfDocument = PdfUtils.insertMetadata(pdfDocument, metadata);

        Map<String, String> extractedMetadata = PdfUtils.extractMetadata(pdfDocument);

        String firstExpectedValue = "testvalue1";
        String secondExpectedValue = "testvalue2";

        Assert.assertEquals(firstExpectedValue, extractedMetadata.get("testkey1"));
        Assert.assertEquals(secondExpectedValue, extractedMetadata.get("testkey2"));
    }

    /**
     * Test of getNumberOfPages method, of class PdfUtils.
     */
    @Test
    public void testGetNumberOfPages() {
        System.out.println("getNumberOfPages");
        byte[] document = pdfDocument;
        int expResult = 2;
        int result = PdfUtils.getNumberOfPages(document);
        Assert.assertEquals(expResult, result);
    }

}
