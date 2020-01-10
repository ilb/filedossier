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
package ru.ilb.filedossier.mappers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.UriBuilder;

import ru.ilb.filedossier.api.DossierResource;
import ru.ilb.filedossier.entities.Dossier;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.view.DossierFileView;
import ru.ilb.filedossier.view.DossierView;

@Named
public class DossierMapperImpl implements DossierMapper {

    private Dossier model;
    private URI dossierResourceUri;
    private final DossierFileMapper dossierFileMapper;

    @Inject
    public DossierMapperImpl(DossierFileMapper dossierFileMapper) {
        this.dossierFileMapper = dossierFileMapper;
    }

    @Override
    public DossierView map() {
        DossierView view = new DossierView();
        view.setCode(model.getCode());
        view.setName(model.getName());
        view.setValid(String.valueOf(model.isValid()));
        view.setDossierFiles(buildDossierFiles(model.getDossierFiles()));
        return view;
    }

    @Override
    public DossierMapper withModel(Dossier model) {
        this.model = model;
        return this;
    }

    @Override
    public DossierMapper withResourceUri(URI dossierResourceUri) {
        this.dossierResourceUri = dossierResourceUri;
        return this;
    }

    private List<DossierFileView> buildDossierFiles(List<DossierFile> dossierFilesModels) {
        return dossierFilesModels.stream()
                .map(fileModel -> dossierFileMapper
                        .withModel(fileModel)
                        .withResourceUri(getDossierFileResourceUri(dossierResourceUri, fileModel.getCode()))
                        .map())
                .collect(Collectors.toList());
    }

    private URI getDossierFileResourceUri(URI baseUri, String code) {
        return baseUri.resolve(UriBuilder.fromMethod(DossierResource.class, "getDossierFileResource")
                        .resolveTemplate("fileCode", code)
                        .build());
    }
}
