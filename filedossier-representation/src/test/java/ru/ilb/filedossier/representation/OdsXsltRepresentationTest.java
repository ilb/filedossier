/*
 * Copyright 2019 slavb.
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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author slavb
 */
public class OdsXsltRepresentationTest {

    public OdsXsltRepresentationTest() {
    }

    /**
     * Test of getContents method, of class OdsXsltRepresentation.
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    @Test
    public void testGetContentsXml() throws URISyntaxException, IOException {
        System.out.println("getContents");
        
        URI stylesheet = getClass().getClassLoader().getResource("fairpriceorder/content.xsl").toURI();
        URI dataUri = getClass().getClassLoader().getResource("fairpriceorder/data.xml").toURI();
        URI template = getClass().getClassLoader().getResource("fairpriceorder/template.ods").toURI();
        
        byte[] source = Files.readAllBytes(Paths.get(dataUri));
        DossierContentsHolder contents = new DossierContentsHolder(source, "application/xml","fairpriceorder", "Отчет","xml");

        OdsXsltRepresentation instance = new OdsXsltRepresentation("application/vnd.oasis.opendocument.spreadsheet", stylesheet, template);
        instance.setParent(contents);
        
        byte[] result = instance.getContents();
        assertNotNull(result);
        assertEquals("fairpriceorder.ods",instance.getFileName());
        assertEquals("application/vnd.oasis.opendocument.spreadsheet", instance.getMediaType());
        assertEquals(34112,result.length);
//        URI testUri = getClass().getClassLoader().getResource("fairpriceorder/test.ods").toURI();
//        byte[] expResult = Files.readAllBytes(Paths.get(testUri));
//        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getContents method, of class OdsXsltRepresentation.
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    @Test
    public void testGetContentsJson() throws URISyntaxException, IOException {
        System.out.println("getContents");

        URI stylesheet = getClass().getClassLoader().getResource("fairpriceorder/content.xsl").toURI();
        URI dataUri = getClass().getClassLoader().getResource("fairpriceorder/data.json").toURI();
        URI template = getClass().getClassLoader().getResource("fairpriceorder/template.ods").toURI();

        byte[] source = Files.readAllBytes(Paths.get(dataUri));
        DossierContentsHolder contents = new DossierContentsHolder(source, "application/json","fairpriceorder", "Отчет","json");

        OdsXsltRepresentation instance = new OdsXsltRepresentation("application/vnd.oasis.opendocument.spreadsheet", stylesheet, template);
        instance.setParent(contents);

        byte[] result = instance.getContents();
        assertNotNull(result);
        assertEquals("fairpriceorder.ods",instance.getFileName());
        assertEquals("application/vnd.oasis.opendocument.spreadsheet", instance.getMediaType());
        assertEquals(34112,result.length);
//        URI testUri = getClass().getClassLoader().getResource("fairpriceorder/test.ods").toURI();
//        byte[] expResult = Files.readAllBytes(Paths.get(testUri));
//        assertArrayEquals(expResult, result);
    }

}
