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

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author slavb
 */
public class DossierImplTest {

    public DossierImplTest() {
    }

    /**
     * Test of addFile method, of class DossierImpl.
     */
    @org.junit.Test
    public void testAddFile() {
        System.out.println("addFile");
        DossierFileImpl file = null;
        DossierImpl instance = new DossierImpl();
        instance.addFile(file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFiles method, of class DossierImpl.
     */
    @org.junit.Test
    public void testGetFiles() {
        System.out.println("getFiles");
        DossierImpl instance = new DossierImpl();
        ArrayList<DossierFileImpl> expResult = null;
        ArrayList<DossierFileImpl> result = instance.getFiles();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
