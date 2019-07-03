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

import javax.inject.Inject;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import ru.ilb.filedossier.api.DossierFileResource;
import ru.ilb.filedossier.api.DossierResource;
import ru.ilb.filedossier.core.DossierFactory;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.mappers.DossierMapper;
import ru.ilb.filedossier.view.DossierView;

public class DossierResourceImpl implements DossierResource {

    private final String dossierKey;

    private final String dossierPackage;

    private final String dossierCode;

    @Inject
    private DossierFactory dossierFactory;

    @Inject
    private DossierMapper dossierMapper;

    @Context
    private ResourceContext resourceContext;

    private ru.ilb.filedossier.entities.Dossier dossier;

    public DossierResourceImpl(String dossierKey, String dossierPackage, String dossierCode) {
        this.dossierKey = dossierKey;
        this.dossierPackage = dossierPackage;
        this.dossierCode = dossierCode;
    }

    private ru.ilb.filedossier.entities.Dossier getDossierInternal() {
        // lazy loading
        if (this.dossier == null) {
            this.dossier = dossierFactory.getDossier(dossierKey, dossierPackage, dossierCode);
        }
        return this.dossier;
    }

    public void setDossierFactory(DossierFactory dossierFactory) {
        this.dossierFactory = dossierFactory;
    }

    public void setDossierMapper(DossierMapper dossierMapper) {
        this.dossierMapper = dossierMapper;
    }

    @Override
    public DossierView getDossier() {
        return dossierMapper.fromModel(getDossierInternal());
    }

    @Override
    public DossierFileResource getDossierFileResource(String fileCode) {
        //можно перенести инициализацию в конструктор
        DossierFile dossierFile = this.getDossierInternal().getDossierFile(fileCode);
        DossierFileResourceImpl resource = new DossierFileResourceImpl(dossierFile);
        return resourceContext.initResource(resource);
    }

}
