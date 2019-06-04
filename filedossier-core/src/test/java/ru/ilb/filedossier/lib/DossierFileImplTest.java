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

import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.Dossier;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static ru.ilb.filedossier.lib.DossierFactoryTest.getDossierFactory;

/**
 *
 * @author slavb
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DossierFileImplTest {
    private final DossierFactory dossierFactory;

    private final Dossier dossier;

    private final DossierFile dossierFile1;
    private final DossierFile dossierFile2;


    public DossierFileImplTest() {
        dossierFactory = getDossierFactory();
        dossier = dossierFactory.getDossier("teststorekey", "testmodel", "TEST");
        dossierFile1 = dossier.getDossierFile("file1");
        dossierFile2 = dossier.getDossierFile("fairpricecalc");
    }

    /**
     * Test of getCode method, of class DossierFileImpl.
     */
    @Test
    public void testGetCode() {
        System.out.println("getCode");
        String expResult = "file1";
        String result = dossierFile1.getCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class DossierFileImpl.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        String expResult = "Файл 1";
        String result = dossierFile1.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRequired method, of class DossierFileImpl.
     */
    @Test
    public void testGetRequired() {
        System.out.println("getRequired");
        boolean expResult = true;
        boolean result = dossierFile1.getRequired();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReadonly method, of class DossierFileImpl.
     */
    @Test
    public void testGetReadonly() {
        System.out.println("getReadonly");
        boolean expResult = false;
        boolean result = dossierFile1.getReadonly();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHidden method, of class DossierFileImpl.
     */
    @Test
    public void testGetHidden() {
        System.out.println("getHidden");
        boolean expResult = false;
        boolean result = dossierFile1.getHidden();
        assertEquals(expResult, result);
    }

    /**
     * Test of getExists method, of class DossierFileImpl.
     */
    @Test
    public void ztestGetExists() {
        System.out.println("getExists");
        boolean expResult = true;
        boolean result = dossierFile1.getExists();
        assertEquals(expResult, result);
    }

    /**
     * Test of getContents method, of class DossierFileImpl.
     */
    @Test// (expected = FileNotExistsException.class)
    public void ztestGetContents_0args() throws Exception {
        System.out.println("getContents");
        byte[] expResult = "test".getBytes();
        byte[] result = dossierFile1.getContents();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of setContents method, of class DossierFileImpl.
     */
    @Test
    public void testPutContents() throws Exception {
        System.out.println("putContents");
        byte[] data = "test".getBytes();
        dossierFile1.setContents(data);
    }

    /**
     * Test of getMediaType method, of class DossierFileImpl.
     */
    @Test
    public void testGetMediaType() {
        System.out.println("getMediaType");
        String expResult = "application/xml";
        String result = dossierFile1.getMediaType();
        assertEquals(expResult, result);
    }

}
