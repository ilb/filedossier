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

import ru.ilb.filedossier.metadata.extractor.ImageUtils;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author kuznetsov_me
 */
public class ImageUtilsTest {

    public ImageUtilsTest() {
    }

    /**
     * Test of extractMetadata method, of class ImageUtils.
     */
    @Test
    public void testExtractMetadata() throws Exception {
        System.out.println("extractMetadata");

        String expectedBarcode = "doctree:11f462ebdb14a5673ff41a5c75c5176552fad343:2:2";
        String expectedSignatures = "16:114:101:129;110:114:195:129;16:267:101:284;110:267:195:284";

        byte[] rawImage = Files.readAllBytes(Paths.get(
                getClass().getClassLoader().getResource("image.jpg").toURI()));

        String resultBarcode = ImageUtils.extractXMPMetadata(rawImage, "barcode");
        String resultSignatures = ImageUtils.extractXMPMetadata(rawImage, "signatures");
        Assert.assertEquals(expectedBarcode, resultBarcode);
        Assert.assertEquals(expectedSignatures, resultSignatures);
    }

}
