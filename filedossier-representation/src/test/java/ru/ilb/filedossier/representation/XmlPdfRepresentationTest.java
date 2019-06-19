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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import ru.ilb.filedossier.representation.XmlPdfRepresentation;
import static org.junit.Assert.*;

/**
 *
 * @author kuznetsov_me
 */
public class XmlPdfRepresentationTest {

    public XmlPdfRepresentationTest() {
    }

    /**
     * Test of getContents method, of class XmlPdfRepresentation.
     */
    @Test
    public void testGetContents() throws URISyntaxException, IOException {
	System.out.println("getContents");
	XmlPdfRepresentation instance = new XmlPdfRepresentation("application/pdf",
		getClass().getClassLoader().getResource("jurnals/example.xml").toURI());
	byte[] result = instance.getContents();
	assertNotNull(result);
	Files.write(Paths.get(System.getProperty("java.io.tmpdir") + "/result"), result);
    }

    /**
     * Test of getExtension method, of class XmlPdfRepresentation.
     */
    @Test
    public void testGetExtension() throws URISyntaxException {
	System.out.println("getExtension");
	XmlPdfRepresentation instance = new XmlPdfRepresentation("application/pdf",
		getClass().getClassLoader().getResource("jurnals/example.xml").toURI());
	String expResult = "pdf";
	String result = instance.getExtension();
	assertEquals(expResult, result);
    }

    /**
     * Test of getMediaType method, of class XmlPdfRepresentation.
     */
    @Test
    public void testGetMediaType() throws URISyntaxException {
	System.out.println("getMediaType");
	XmlPdfRepresentation instance = new XmlPdfRepresentation("application/pdf",
		getClass().getClassLoader().getResource("jurnals/example.xml").toURI());
	String expResult = "application/pdf";
	String result = instance.getMediaType();
	assertEquals(expResult, result);
    }

}
