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

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import ru.ilb.filedossier.ddl.DossierDefinition;
import ru.ilb.filedossier.ddl.DossierFileDefinition;
import ru.ilb.filedossier.ddl.DossierDefinitionRepository;
import ru.ilb.filedossier.entities.Dossier;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.entities.Store;
import ru.ilb.filedossier.representation.RepresentationFactory;
import ru.ilb.filedossier.store.StoreFactory;

/**
 *
 * @author slavb
 */
@Named
public class DossierFactory {

    private DossierDefinitionRepository dossierDefinitionRepository;

    private StoreFactory storeFactory;

    private RepresentationFactory representationFactory;

    @Inject
    public DossierFactory(DossierDefinitionRepository dossierDefinitionRepository,
            StoreFactory storeFactory) {
        this.dossierDefinitionRepository = dossierDefinitionRepository;
        this.storeFactory = storeFactory;
    }

    public Dossier getDossier(String dossierKey, String dossierPackage, String dossierCode) {

        DossierDefinition dossierModel = dossierDefinitionRepository
                .getDossierDefinition(dossierPackage, dossierCode);

        URI baseDefinitionUri = dossierDefinitionRepository
                .getDossierDefinitionUri(dossierPackage);

        Store store = storeFactory.getStore(dossierKey);
        System.out.println("dossierCode:" + dossierCode);
        System.out.println("dossierKey: " + dossierKey);

        if (representationFactory == null) {
            representationFactory = new RepresentationFactory(store, baseDefinitionUri);
        }
        return createDossier(dossierModel, store, dossierKey, dossierPackage);
    }

    private Dossier createDossier(DossierDefinition model, Store store, String dossierKey,
            String dossierPackage) {

        List<DossierFile> dossierFiles = model.getDossierFiles().stream()
                .filter(fileModel -> fileModel.getRepresentations().size() > 0)
                .map(fileModel -> createDossierFile(fileModel, store))
                .collect(Collectors.toList());
        return new DossierImpl(model.getCode(), model.getName(), dossierPackage, dossierKey,
                dossierFiles);
    }

    private DossierFile createDossierFile(DossierFileDefinition model, Store store) {
        List<Representation> representations = model.getRepresentations().stream()
                .map(representationModel
                        -> representationFactory.createRepresentation(representationModel))
                .collect(Collectors.toList());
        return new DossierFileImpl(store,
                model.getCode(), model.getName(), model.getRequired(), model.getReadonly(),
                model.getHidden(), model.getMediaType(), representations);
    }
}
