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
import ru.ilb.filedossier.scripting.TemplateEvaluator;
import ru.ilb.filedossier.store.StoreFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import ru.ilb.filedossier.ddl.DossierNotFoundException;
import ru.ilb.filedossier.ddl.PackageDefinition;

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

    private TemplateEvaluator templateEvaluator;

    @Inject
    public DossierFactory(DossierDefinitionRepository dossierDefinitionRepository,
                          StoreFactory storeFactory, DossierContextService contextService) {

        this.dossierDefinitionRepository = dossierDefinitionRepository;
        this.storeFactory = storeFactory;
        this.contextService = contextService;
    }

    public Dossier getDossier(String dossierKey, String dossierPackage, String dossierCode, String dossierMode) {

        contextRoot = String.format("%s/%s", dossierKey, dossierCode);

        final PackageDefinition dossierPackageDefinition = dossierDefinitionRepository
                .getDossierPackage(dossierPackage, dossierMode);

        DossierDefinition dossierModel = dossierPackageDefinition.getDossiers().stream()
                .filter(d -> d.getCode().equals(dossierCode)).findFirst().orElseThrow(() -> new DossierNotFoundException(dossierCode));

        final Store store = storeFactory.getStore(dossierKey);

        if (representationFactory == null) {
            representationFactory = new RepresentationFactory(
                    dossierPackageDefinition.getBaseUri() /* , templateEvaluator */);
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

    private DossierFile createDossierFile(DossierFileDefinition model, Store store) throws IOException {
        DossierContext context = contextService.getContext(String.format("%s/%s", contextRoot, model.getCode()));

        return DossierFileFactory.createDossierFile(
                store, representationFactory,
                model, context);
    }
}
