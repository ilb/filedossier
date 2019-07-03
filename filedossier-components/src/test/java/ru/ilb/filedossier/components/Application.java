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
package ru.ilb.filedossier.components;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
@ComponentScan(basePackages = { "ru.ilb.filedossier.components", "ru.ilb.filedossier.mappers" })
public class Application {

    @Bean
    public DossierFactory dossierFactory() throws NamingException {
        DossierDefinitionRepository dossierModelRepository;
        StoreFactory storeFactory;
        dossierModelRepository = new FileDossierDefinitionRepository(Paths.get("/tmp/store").resolve("packages").toUri());
        storeFactory = StoreFactory.newInstance(Paths.get("/tmp/store").toUri());

        DossierContextBuilder dossierContextBuilder = (String dossierKey, String dossierPackage, String dossierCode) -> {
            DossierContext dc = new DossierContextImpl("");
            dc.setProperty("name", "Тест имя");
            dc.setProperty("prop", false);
            return dc;
        };
        TemplateEvaluator templateEvaluator = new SubstitutorTemplateEvaluator(new InitialContext());
        return new DossierFactory(dossierModelRepository, storeFactory, dossierContextBuilder, templateEvaluator);

    }

}
