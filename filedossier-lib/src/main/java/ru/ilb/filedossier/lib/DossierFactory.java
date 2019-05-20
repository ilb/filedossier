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

import java.util.List;
import java.util.stream.Collectors;
import ru.ilb.filedossier.model.DossierModel;
import ru.ilb.filedossier.model.DossierModelFile;
import ru.ilb.filedossier.store.Store;
import ru.ilb.filedossier.store.StoreFactory;

/**
 *
 * @author slavb
 */
public class DossierFactory {

    private final DossierModelRepository dossierModelRepository;

    private final StoreFactory storeFactory;

    public DossierFactory(DossierModelRepository dossierModelRepository, StoreFactory storeFactory) {
        this.dossierModelRepository = dossierModelRepository;
        this.storeFactory = storeFactory;
    }

    public Dossier createDossier(String dossierKey, String dossierCode) {
        DossierModel dossierModel = dossierModelRepository.getDossierModel(dossierCode);
        Store store = storeFactory.getFileStorage(dossierKey);
        return createDossier(dossierModel, store);
    }

    private Dossier createDossier(DossierModel dossierModel, Store store) {
        String code = dossierModel.getCode();
        String name = dossierModel.getName();

        List<DossierModelFile> modelFiles = dossierModel.getDossierModelFiles();

        List<DossierFile> dossierFiles = dossierModel.getDossierModelFiles().stream().map(modelFile -> new DossierFileImpl(
                store, modelFile.getCode(), modelFile.getName(),
                Boolean.TRUE.equals(modelFile.getRequired()), Boolean.TRUE.equals(modelFile.getReadonly()),
                Boolean.TRUE.equals(modelFile.getVisible()), store.isExist(modelFile.getCode())))
                .collect(Collectors.toList());

        return new DossierImpl(code, name, dossierFiles);
    }

}
