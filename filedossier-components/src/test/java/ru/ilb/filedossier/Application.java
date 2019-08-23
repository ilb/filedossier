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
package ru.ilb.filedossier;

import java.net.URISyntaxException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.cxf.jaxrs.provider.json.JsonMapObjectProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ru.ilb.filedossier.context.DossierContextBuilder;
import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.core.DossierFactory;
import ru.ilb.filedossier.ddl.DossierDefinitionRepository;
import ru.ilb.filedossier.ddl.FileDossierDefinitionRepository;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.scripting.SubstitutorTemplateEvaluator;
import ru.ilb.filedossier.scripting.TemplateEvaluator;
import ru.ilb.filedossier.store.StoreFactory;

/**
 *
 * @author slavb
 */
@SpringBootApplication
// @EnableJdbcRepositories(basePackages =
// "ru.ilb.filedossier.context.persistence.repositories")
@ComponentScan
public class Application {

    // @Bean
    // public DossierContextService dossierContextService() {
    // return new DBDossierContextService();
    // }
    //
    // @Bean
    // public NamingStrategy namingStrategy() {
    // return new DossierContextNamingStrategy();
    // }
    @Bean
    public JsonMapObjectProvider jsonMapObjectProvider() {
        return new JsonMapObjectProvider();
    }

    @Bean
    public DossierFactory dossierFactory() throws NamingException {
        DossierDefinitionRepository dossierModelRepository;
        StoreFactory storeFactory;
        try {
            dossierModelRepository = new FileDossierDefinitionRepository(
                    Application.class.getClassLoader().getResource("models").toURI());
            storeFactory = StoreFactory
                    .newInstance(
                            Application.class.getClassLoader().getResource("teststoreroot").toURI());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        DossierContextBuilder dossierContextBuilder = (String dossierKey, String dossierPackage,
                String dossierCode) -> {
            DossierContext dc = new DossierContextImpl();
            dc.setProperty("name", "Тест имя");
            dc.setProperty("prop", false);
            return dc;
        };
        TemplateEvaluator templateEvaluator = new SubstitutorTemplateEvaluator(new InitialContext());
        return new DossierFactory(dossierModelRepository, storeFactory, dossierContextBuilder,
                templateEvaluator);

    }

}
