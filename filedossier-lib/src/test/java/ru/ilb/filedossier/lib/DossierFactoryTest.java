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

import ru.ilb.filedossier.entities.Dossier;
import ru.ilb.filedossier.context.DossierContextBuilder;
import java.net.URISyntaxException;
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

    private final DossierFactory dossierFactory;

    public static DossierFactory getDossierFactory() {
        DossierModelRepository dossierModelRepository;
        StoreFactory storeFactory;
        try {
            dossierModelRepository = new FileDossierModelRepository(DossierFactoryTest.class.getClassLoader().getResource("models").toURI());
            storeFactory = StoreFactory.newInstance(DossierFactoryTest.class.getClassLoader().getResource("teststoreroot").toURI());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        DossierContextBuilder dossierContextBuilder = (String dossierKey, String dossierCode) -> {
            DossierContext dc = new DossierContext();
            dc.setProperty("name", "Тест имя");
            dc.setProperty("prop", false);
            return dc;
        };
        TemplateEvaluator templateEvaluator = new SubstitutorTemplateEvaluator();
        return new DossierFactory(dossierModelRepository, storeFactory, dossierContextBuilder, templateEvaluator);

    }

    public DossierFactoryTest() {
        dossierFactory = getDossierFactory();
    }

    /**
     * Test of getDossier method, of class DossierFactory.
     */
    @Test
    public void testCreateDossier() {
        System.out.println("createDossier");
        String dossierKey = "123";
        String dossierPackage = "testmodel";
        String dossierCode = "TEST";

        String expResult = "Тест имя";
        Dossier result = dossierFactory.getDossier(dossierKey, dossierPackage, dossierCode);
        assertEquals(expResult, result.getDossierFile("fairpricecalc").getName());
    }

}
