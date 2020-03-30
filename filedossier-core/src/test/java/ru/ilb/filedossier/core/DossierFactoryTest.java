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
package ru.ilb.filedossier.core;

import org.junit.ClassRule;
import org.junit.Test;
import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.context.DossierContextService;
import ru.ilb.filedossier.ddl.FileDossierDefinitionRepository;
import ru.ilb.filedossier.entities.Dossier;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.jndi.JndiRule;
import ru.ilb.filedossier.store.StoreFactory;

import javax.naming.Context;
import javax.naming.NamingException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import ru.ilb.jndicontext.core.SimpleInitialContext;

/**
 *
 * @author slavb
 */
public class DossierFactoryTest {

    private final DossierFactory dossierFactory;

    @ClassRule
    public static JndiRule jndi = new JndiRule() {
        @Override
        protected void bind(Context context) throws NamingException {
            context.bind("ru.bystrobank.apps.meta.url", "https://devel.net.ilb.ru/meta");
        }

    };

    public static DossierFactory getDossierFactory() {
        FileDossierDefinitionRepository dossierModelRepository;
        StoreFactory storeFactory;
        try {
            dossierModelRepository = new FileDossierDefinitionRepository(
                    DossierFactoryTest.class.getClassLoader().getResource("models").toURI());
            storeFactory = StoreFactory
                    .newInstance(DossierFactoryTest.class.getClassLoader().getResource("teststoreroot").toURI());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        Context context = new SimpleInitialContext();
        try {
            context.bind("ru.bystrobank.apps.meta.url", "https://devel.net.ilb.ru/meta");
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
        //TemplateEvaluator templateEvaluator = new SubstitutorTemplateEvaluator(context);
        return new DossierFactory(dossierModelRepository, storeFactory, new DossierContextService() {
            @Override
            public DossierContext getContext(String contextKey) {
                return new DossierContextImpl();
            }

            @Override
            public void putContext(String contextKey, DossierContext context) {

            }
        } /*, templateEvaluator*/);

    }

    public DossierFactoryTest() throws NamingException {
        dossierFactory = getDossierFactory();
    }

    /**
     * Test of getDossier method, of class DossierFactory.
     */
    @Test
    public void testCreateDossier() {
        System.out.println("createDossier");
        String dossierKey = "teststorekey";
        String dossierPackage = "testmodel";
        String dossierCode = "TEST";
        String dossierMode = "mode1";

        String expResult = "Тест имя";
        Dossier result = dossierFactory.getDossier(dossierKey, dossierPackage, dossierCode, dossierMode);
        assertEquals(expResult, result.getDossierFile("fairpricecalc").getName());
    }

}
