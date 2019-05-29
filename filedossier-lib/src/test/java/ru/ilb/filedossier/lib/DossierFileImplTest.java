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
package ru.ilb.filedossier.lib;

import org.junit.Test;
import static org.junit.Assert.*;
import static ru.ilb.filedossier.lib.DossierFactoryTest.getDossierFactory;

/**
 *
 * @author slavb
 */
public class DossierFileImplTest {
    private final DossierFactory dossierFactory;

    private final Dossier dossier;

    private final DossierFile dossierFile;


    public DossierFileImplTest() {
        dossierFactory = getDossierFactory();
        dossier = dossierFactory.getDossier("teststorekey", "testmodel");
        dossierFile = dossier.getDossierFile("file1");
    }

    /**
     * Test of getCode method, of class DossierFileImpl.
     */
    @Test
    public void testGetCode() {
        System.out.println("getCode");
        String expResult = "file1";
        String result = dossierFile.getCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class DossierFileImpl.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        String expResult = "Файл 1";
        String result = dossierFile.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRequired method, of class DossierFileImpl.
     */
    @Test
    public void testGetRequired() {
        System.out.println("getRequired");
        boolean expResult = true;
        boolean result = dossierFile.getRequired();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReadonly method, of class DossierFileImpl.
     */
    @Test
    public void testGetReadonly() {
        System.out.println("getReadonly");
        boolean expResult = false;
        boolean result = dossierFile.getReadonly();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHidden method, of class DossierFileImpl.
     */
    @Test
    public void testGetHidden() {
        System.out.println("getHidden");
        boolean expResult = false;
        boolean result = dossierFile.getHidden();
        assertEquals(expResult, result);
    }

    /**
     * Test of getExists method, of class DossierFileImpl.
     */
    @Test
    public void testGetExists() {
        System.out.println("getExists");
        boolean expResult = true;
        boolean result = dossierFile.getExists();
        assertEquals(expResult, result);
    }

    /**
     * Test of getContents method, of class DossierFileImpl.
     */
    @Test// (expected = FileNotExistsException.class)
    public void testGetContents_0args() throws Exception {
        System.out.println("getContents");
        byte[] expResult = "test".getBytes();
        byte[] result = dossierFile.getContents();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getContents method, of class DossierFileImpl.
     */
    @Test // (expected = FileNotExistsException.class)
    public void testGetContents_String() throws Exception {
        System.out.println("getContents");
        String mediaType = "application/xml";
        byte[] expResult = "test".getBytes();
        byte[] result = dossierFile.getContents(mediaType);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of putContents method, of class DossierFileImpl.
     */
    @Test
    public void testPutContents() throws Exception {
        System.out.println("putContents");
        byte[] data = "test".getBytes();
        dossierFile.putContents(data);
    }

    /**
     * Test of getMediaType method, of class DossierFileImpl.
     */
    @Test
    public void testGetMediaType() {
        System.out.println("getMediaType");
        String expResult = "application/xml";
        String result = dossierFile.getMediaType();
        assertEquals(expResult, result);
    }

}
