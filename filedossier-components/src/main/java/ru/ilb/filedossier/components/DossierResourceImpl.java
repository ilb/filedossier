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
import javax.inject.Named;
import ru.ilb.filedossier.api.DossierFileResource;
import ru.ilb.filedossier.api.DossierResource;
import ru.ilb.filedossier.entities.Dossier;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.mappers.DossierMapper;
import ru.ilb.filedossier.view.DossierView;

@Named
public class DossierResourceImpl implements DossierResource {

    @Inject
    private DossierMapper dossierMapper;

    @Inject
    private DossierFileResourceImpl resource;

    private Dossier dossier;

    @Override
    public DossierView getDossier() {
        return dossierMapper.fromModel(dossier);
    }

    @Override
    public DossierFileResource getDossierFileResource(String fileCode) {
        DossierFile dossierFile = dossier.getDossierFile(fileCode);
        resource.setDossierFile(dossierFile);
        return resource;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }
}
