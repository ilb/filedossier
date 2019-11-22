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

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author slavb
 */
public class FileDossierDefinitionRepositoryTest {

    public FileDossierDefinitionRepositoryTest() {
    }

    /**
     * Test of getDossierPackage method, of class FileDossierDefinitionRepository.
     *
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testGetDossierDefinitionXml() throws URISyntaxException {
        String dossierPackage = "testmodel";
        String dossierCode = "TEST";
        String dossierMode = "mode1";
        URI modelsUri = getClass().getClassLoader().getResource("models").toURI();

        DossierDefinitionRepository instance = new FileDossierDefinitionRepository(modelsUri);

        PackageDefinition dossierPackageDefinition = instance.getDossierPackage(dossierPackage, dossierMode);

        DossierDefinition result = dossierPackageDefinition.getDossiers().stream()
                .filter(d -> d.getCode().equals(dossierCode)).findFirst().orElseThrow(() -> new DossierNotFoundException(dossierCode));

        assertEquals("TEST", result.getCode());
        assertEquals("Тестовое досье", result.getName());
        assertEquals(2, result.getDossierFiles().size());
    }

    @Test
    public void testGetDossierDefinitionXsl() throws URISyntaxException {
        String dossierPackage = "testmodelxsl";
        String dossierCode = "TEST";
        String dossierMode = "mode1";
        URI modelsUri = getClass().getClassLoader().getResource("models").toURI();

        DossierDefinitionRepository instance = new FileDossierDefinitionRepository(modelsUri);

        PackageDefinition dossierPackageDefinition = instance.getDossierPackage(dossierPackage, dossierMode);

        DossierDefinition dossierDefinition = dossierPackageDefinition.getDossiers().stream()
                .filter(d -> d.getCode().equals(dossierCode)).findFirst().orElseThrow(() -> new DossierNotFoundException(dossierCode));

        assertEquals("TEST", dossierDefinition.getCode());
        assertEquals("Тестовое досье", dossierDefinition.getName());
        assertEquals(2, dossierDefinition.getDossierFiles().size());

        String fileCode="fairpricecalc";

        DossierFileDefinition dossierFileDefinition = dossierDefinition.getDossierFiles().stream()
                .filter(d -> d.getCode().equals(fileCode)).findFirst().orElseThrow(() -> new DossierNotFoundException(dossierCode));

        assertEquals(true,dossierFileDefinition.getRequired());
    }

    @Test
    public void testGetDossierDefinitionXsl2() throws URISyntaxException {
        String dossierPackage = "testmodelxsl";
        String dossierCode = "TEST";
        String dossierMode = "mode2";
        URI modelsUri = getClass().getClassLoader().getResource("models").toURI();

        DossierDefinitionRepository instance = new FileDossierDefinitionRepository(modelsUri);

        PackageDefinition dossierPackageDefinition = instance.getDossierPackage(dossierPackage, dossierMode);

        DossierDefinition dossierDefinition = dossierPackageDefinition.getDossiers().stream()
                .filter(d -> d.getCode().equals(dossierCode)).findFirst().orElseThrow(() -> new DossierNotFoundException(dossierCode));

        assertEquals("TEST", dossierDefinition.getCode());
        assertEquals("Тестовое досье", dossierDefinition.getName());
        assertEquals(2, dossierDefinition.getDossierFiles().size());

        String fileCode="fairpricecalc";

        DossierFileDefinition dossierFileDefinition = dossierDefinition.getDossierFiles().stream()
                .filter(d -> d.getCode().equals(fileCode)).findFirst().orElseThrow(() -> new DossierNotFoundException(dossierCode));

        assertEquals(false,dossierFileDefinition.getRequired());
    }

}
