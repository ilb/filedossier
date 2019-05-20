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
import ru.ilb.filedossier.model.DossierModel;

/**
 *
 * @author slavb
 */
public class DossierModelRepositoryTest {

    public DossierModelRepositoryTest() {
    }

    /**
     * Test of getDossierModel method, of class DossierModelRepository.
     */
    @Test
    public void testGetDossierModel() {
        System.out.println("getDossierModel");
        String dossierCode = "testmodel";
        DossierModelRepository instance = new DossierModelRepository("src/test/resources/models");
        DossierModel result = instance.getDossierModel(dossierCode);
        assertEquals("TEST", result.getCode());
        assertEquals("Тестовое досье", result.getName());
        assertEquals(2, result.getDossierModelFiles().size());
    }

}
