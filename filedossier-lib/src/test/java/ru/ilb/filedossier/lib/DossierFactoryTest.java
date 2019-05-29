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

import ru.ilb.filedossier.context.DossierContextBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.ilb.filedossier.context.DossierContext;
import ru.ilb.filedossier.model.FileDossierModelRepository;
import ru.ilb.filedossier.model.DossierModelRepository;
import ru.ilb.filedossier.scripting.SubstitutorTemplateEvaluator;
import ru.ilb.filedossier.scripting.TemplateEvaluator;
import ru.ilb.filedossier.store.StoreFactory;

/**
 *
 * @author slavb
 */
public class DossierFactoryTest {

    private Path basePath = Paths.get("").toAbsolutePath().getParent();

    private DossierModelRepository dossierModelRepository;

    private StoreFactory storeFactory;

    private DossierContextBuilder dossierContextBuilder = new DossierContextBuilder() {
        @Override
        public DossierContext createDossierContext(String dossierKey, String dossierCode) {
            DossierContext dc = new DossierContext();
            dc.setProperty("name", "Тест имя");
            dc.setProperty("prop", false);
            return dc;
        }
    };

    TemplateEvaluator templateEvaluator = new SubstitutorTemplateEvaluator();

    public DossierFactoryTest() throws URISyntaxException {
        URI modelsUri = getClass().getClassLoader().getResource("models").toURI();
        URI storeUri = getClass().getClassLoader().getResource("teststoreroot").toURI();

        dossierModelRepository = new FileDossierModelRepository(modelsUri);
        storeFactory = StoreFactory.newInstance(storeUri);

    }

    /**
     * Test of createDossier method, of class DossierFactory.
     */
    @Test
    public void testCreateDossier() {
        System.out.println("createDossier");
        String dossierKey = "123";
        String dossierCode = "testmodel";
        DossierFactory instance = new DossierFactory(dossierModelRepository, storeFactory, dossierContextBuilder, templateEvaluator);
        Dossier expResult = null;
        Dossier result = instance.createDossier(dossierKey, dossierCode);
        assertEquals("Тест имя", result.getDossierFile("file2").getName());
    }

}
