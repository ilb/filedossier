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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import ru.ilb.filedossier.entities.Store;
import ru.ilb.filedossier.store.StoreFactory;

/**
 *
 * @author kuznetsov_me
 */
public class PdfMultipageRepresentationTest {

    URI pagesUri;

    public PdfMultipageRepresentationTest() throws URISyntaxException {
        this.pagesUri = getClass().getClassLoader().getResource("pages").toURI();
    }

    /**
     * Test of getContents method, of class PdfMultipageRepresentation.
     */
    @Test
    public void testGetContents() throws IOException {
        System.out.println("getContents");

        Store store = StoreFactory.newInstance(pagesUri).getStore("pages");

        PdfMultipageRepresentation instance = new PdfMultipageRepresentation("application/pdf",
                                                                             store);
        byte[] result = instance.getContents();

        assertNotNull(result);

        Files.write(Paths.get(System.getProperty("java.io.tmpdir") + "/MultipageRepresentation.pdf"),
                    result);

    }

    /**
     * Test of getExtension method, of class PdfMultipageRepresentation.
     */
    @Test
    public void testGetExtension() {
        System.out.println("getExtension");
        Store store = StoreFactory.newInstance(pagesUri).getStore("pages");
        PdfMultipageRepresentation instance = new PdfMultipageRepresentation("application/pdf",
                                                                             store);
        String expResult = "pdf";
        String result = instance.getExtension();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getMediaType method, of class PdfMultipageRepresentation.
     */
    @Test
    public void testGetMediaType() {
        System.out.println("getMediaType");
        Store store = StoreFactory.newInstance(pagesUri).getStore("pages");
        PdfMultipageRepresentation instance = new PdfMultipageRepresentation("application/pdf",
                                                                             store);
        String expResult = "application/pdf";
        String result = instance.getMediaType();
        Assert.assertEquals(expResult, result);
    }

}
