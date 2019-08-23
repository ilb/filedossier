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
import org.junit.Test;
import static org.junit.Assert.*;
import ru.ilb.filedossier.ddl.DossierDefinition;
import ru.ilb.filedossier.ddl.DossierDefinitionRepository;

/**
 *
 * @author slavb
 */
public class FileDossierDefinitionRepositoryTest {

    public FileDossierDefinitionRepositoryTest() {
    }

    /**
     * Test of getDossierDefinition method, of class FileDossierDefinitionRepository.
     *
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testGetDossierDefinition() throws URISyntaxException {
        String dossierPackage = "testmodel";
        String dossierCode = "TEST";
        URI modelsUri = getClass().getClassLoader().getResource("models").toURI();

        DossierDefinitionRepository instance = new FileDossierDefinitionRepository(modelsUri);
        DossierDefinition result = instance.getDossierDefinition(dossierPackage, dossierCode);
        assertEquals("TEST", result.getCode());
        assertEquals("Тестовое досье", result.getName());
        assertEquals(2, result.getDossierFiles().size());
    }

}
