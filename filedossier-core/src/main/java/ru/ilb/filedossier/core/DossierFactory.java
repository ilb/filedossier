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

import ru.ilb.filedossier.context.DossierContextService;
import ru.ilb.filedossier.ddl.DossierDefinition;
import ru.ilb.filedossier.ddl.DossierDefinitionRepository;
import ru.ilb.filedossier.ddl.DossierFileDefinition;
import ru.ilb.filedossier.entities.*;
import ru.ilb.filedossier.representation.RepresentationFactory;
import ru.ilb.filedossier.store.StoreFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 *
 * @author slavb
 */
@Named
public class DossierFactory {

    private DossierDefinitionRepository dossierDefinitionRepository;

    private StoreFactory storeFactory;

    private RepresentationFactory representationFactory;

    private DossierContextService contextService;

    private String contextRoot;

    //private TemplateEvaluator templateEvaluator;

    @Inject
    public DossierFactory(DossierDefinitionRepository dossierDefinitionRepository,
                          StoreFactory storeFactory, DossierContextService contextService
            /*,  TemplateEvaluator templateEvaluator */) {
        this.dossierDefinitionRepository = dossierDefinitionRepository;
        this.storeFactory = storeFactory;
        this.contextService = contextService;
        //this.templateEvaluator = templateEvaluator;
    }

    public Dossier getDossier(String dossierKey, String dossierPackage, String dossierCode) {

        contextRoot = dossierCode;

        final DossierDefinition dossierModel = dossierDefinitionRepository
                .getDossierDefinition(dossierPackage, dossierCode);

        final URI baseDefinitionUri = dossierDefinitionRepository
                .getDossierDefinitionUri(dossierPackage);

        final Store store = storeFactory.getStore(dossierKey);

        if (representationFactory == null) {
            representationFactory = new RepresentationFactory(
                    baseDefinitionUri /* , templateEvaluator */);
        }
        return createDossier(dossierModel, store, dossierKey, dossierPackage);
    }

    private Dossier createDossier(DossierDefinition model, Store store, String dossierKey,
            String dossierPackage) {

        final List<DossierFile> dossierFiles = model.getDossierFiles().stream()
                .map(fileModel -> {
                    try {
                        return createDossierFile(fileModel, store);
                    } catch (IOException e) {
                        throw new RuntimeException("Error while creating dossier file");
                    }
                })
                .collect(Collectors.toList());
        return new DossierImpl(model.getCode(), model.getName(), dossierPackage, dossierKey,
                dossierFiles);
    }

    // TODO: evaluate model values
    private DossierFile createDossierFile(DossierFileDefinition model, Store store) throws IOException {
       return DossierFileFactory.createDossierFile(store, representationFactory, model);
    }
}
