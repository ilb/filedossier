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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.ilb.filestorage.StoreFactory;

/**
 *
 * @author slavb
 */
public class DossierFactoryTest {

    DossierModelRepository dossierModelRepository;

    StoreFactory store;

    public DossierFactoryTest() {
        dossierModelRepository = new DossierModelRepository("src/test/resources/models");
        Path tempDirWithPrefix;
        try {
            tempDirWithPrefix = Files.createTempDirectory("dossierFactoryTest");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        store = StoreFactory.newInstance(tempDirWithPrefix.toString());
    }

    /**
     * Test of createDossier method, of class DossierFactory.
     */
    @Test
    public void testCreateDossier() {
        System.out.println("createDossier");
        String dossierKey = "123";
        String dossierCode = "testmodel";
        DossierFactory instance = new DossierFactory(dossierModelRepository, store);
        Dossier expResult = null;
        Dossier result = instance.createDossier(dossierKey, dossierCode);
        assertNotNull(result);;
    }

}
