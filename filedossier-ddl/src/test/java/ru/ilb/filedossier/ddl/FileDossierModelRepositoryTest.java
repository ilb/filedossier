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
package ru.ilb.filedossier.ddl;

import ru.ilb.filedossier.ddl.FileDossierDefinitionRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.ilb.filedossier.ddl.DossierModel;
import ru.ilb.filedossier.ddl.DossierDefinitionRepository;

/**
 *
 * @author slavb
 */
public class FileDossierModelRepositoryTest {

    public FileDossierModelRepositoryTest() {
    }

    /**
     * Test of getDossierModel method, of class FileDossierModelRepository.
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testGetDossierModel() throws URISyntaxException {
        String dossierPackage = "testmodel";
        String dossierCode = "TEST";
        URI modelsUri = getClass().getClassLoader().getResource("models").toURI();

        DossierDefinitionRepository instance = new FileDossierDefinitionRepository(modelsUri);
        DossierModel result = instance.getDossierModel(dossierPackage, dossierCode);
        assertEquals("TEST", result.getCode());
        assertEquals("Тестовое досье", result.getName());
        assertEquals(2, result.getDossierFiles().size());
    }

}
