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
package ru.ilb.filedossier.model;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author slavb
 */
public class DossierModelFileRepositoryTest {

    Path basePath = Paths.get("").toAbsolutePath().getParent();

    public DossierModelFileRepositoryTest() {
    }

    /**
     * Test of getDossierModel method, of class DossierModelFileRepository.
     */
    @Test
    public void testGetDossierModel() {
        String dossierCode = "testmodel";

        DossierModelRepository instance = new DossierModelFileRepository(basePath.resolve("filedossier-model/src/test/resources/models").toUri());
        DossierModel result = instance.getDossierModel(dossierCode);
        assertEquals("TEST", result.getCode());
        assertEquals("Тестовое досье", result.getName());
        assertEquals(2, result.getDossierModelFiles().size());
    }

}
