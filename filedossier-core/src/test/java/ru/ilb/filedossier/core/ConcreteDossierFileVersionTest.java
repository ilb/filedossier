/*
 * Copyright 2020 slavb.
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
package ru.ilb.filedossier.core;

import org.apache.pdfbox.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static ru.ilb.filedossier.core.DossierFactoryTest.getDossierFactory;
import ru.ilb.filedossier.entities.Dossier;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.DossierFileVersion;
import ru.ilb.filedossier.entities.DossierPath;
import ru.ilb.filedossier.entities.Representation;

/**
 *
 * @author slavb
 */
public class ConcreteDossierFileVersionTest {

    private DossierFileVersion instance;

    public ConcreteDossierFileVersionTest() {
        DossierFactory dossierFactory = getDossierFactory();
        Dossier dossier = dossierFactory.getDossier("teststorekey", "testmodel", "TEST", "mode1");
        DossierFile dossierFile1 = dossier.getDossierFile("fairpricecalc");

        instance = dossierFile1.getLatestVersion();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getMediaType method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testGetMediaType() {
        System.out.println("getMediaType");
        String expResult = "application/xml";
        String result = instance.getMediaType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getExtension method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testGetExtension() {
        System.out.println("getExtension");
        String expResult = "xml";
        String result = instance.getExtension();
        assertEquals(expResult, result);
    }

    /**
     * Test of setStore method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testSetStore() {
        System.out.println("setStore");
//        Store store = null;
//        ConcreteDossierFileVersion instance = null;
//        instance.setStore(store);
    }

    /**
     * Test of setMediaType method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testSetMediaType() {
        System.out.println("setMediaType");
//        String mediaType = "";
//        ConcreteDossierFileVersion instance = null;
//        instance.setMediaType(mediaType);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getCode method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testGetCode() {
        System.out.println("getCode");
        String expResult = "fairpricecalc";
        String result = instance.getCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        String expResult = "Тест имя";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getParent method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testGetParent() {
        System.out.println("getParent");
        DossierPath result = instance.getParent();
        assertEquals("fairpricecalc", result.getCode());
    }

    /**
     * Test of setParent method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testSetParent() {
        System.out.println("setParent");
//        DossierPath parent = null;
//        ConcreteDossierFileVersion instance = null;
//        instance.setParent(parent);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setContents method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testSetContents() throws Exception {
        System.out.println("setContents");
        byte[] contents = null;
//        ConcreteDossierFileVersion instance = null;
//        instance.setContents(contents);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getContents method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testGetContents() throws Exception {
        System.out.println("getContents");
        byte[] expResult = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream("teststoreroot/teststorekey/fairpricecalc/0/fairpricecalc"));
        byte[] result = instance.getContents();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getRepresentation method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testGetRepresentationZeroargs() {
        System.out.println("getRepresentation");
        //Representation expResult = null;
        Representation result = instance.getRepresentation();
        assertEquals("application/vnd.oasis.opendocument.spreadsheet", result.getMediaType());
    }

    /**
     * Test of getRepresentation method, of class ConcreteDossierFileVersion.
     */
    @Test
    public void testGetRepresentationString() {
        System.out.println("getRepresentation");
        String mediaType = "application/xml";
        //Representation expResult = null;
        Representation result = instance.getRepresentation(mediaType);
        assertEquals(mediaType, result.getMediaType());
    }

}
