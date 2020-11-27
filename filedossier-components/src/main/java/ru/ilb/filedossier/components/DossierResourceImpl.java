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

import java.util.List;
import java.util.function.Predicate;

import javax.inject.Inject;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.springframework.context.ApplicationContext;

import ru.ilb.filedossier.api.DossierFileResource;
import ru.ilb.filedossier.api.DossierResource;
import ru.ilb.filedossier.api.DossierStoreResource;
import ru.ilb.filedossier.entities.Dossier;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.mappers.DossierMapper;
import ru.ilb.filedossier.view.DossierView;

/**
 * Resource for getting dossiers and dossier files sub resources.
 */
public class DossierResourceImpl implements DossierResource {

    /**
     * DossierMapper is needed for mapping dossier views from models.
     */
    @Inject
    private DossierMapper dossierMapper;

    /**
     * Spring application context.
     */
    @Inject
    private ApplicationContext applicationContext;

    /**
     * JAX-RS resources context.
     */
    @Context
    private ResourceContext resourceContext;

    @Context
    private UriInfo uriInfo;

    /**
     * Dossier model.
     */
    private Dossier dossier;

    final void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    @Override
    public DossierView getDossier(final List<String> codes) {
        Predicate<DossierFile> fileFilter = dossierFile -> codes.isEmpty() || codes.contains(dossierFile.getCode());
        return dossierMapper.map(dossier, uriInfo.getRequestUri(), fileFilter);
    }

    @Override
    public DossierFileResource getDossierFileResource(String fileCode) {
        final DossierFile dossierFile = dossier.getDossierFile(fileCode);
        final DossierFileResourceImpl resource = new DossierFileResourceImpl();
        resource.setDossierFile(dossierFile);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(resource);
        return resourceContext.initResource(resource);
    }

    @Override
    public DossierStoreResource getDossierStoreResource() {
        throw new UnsupportedOperationException(
                "Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }
}
