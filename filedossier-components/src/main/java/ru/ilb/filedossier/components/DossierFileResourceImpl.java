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

import org.springframework.context.ApplicationContext;
import ru.ilb.filedossier.api.DossierContextResource;
import ru.ilb.filedossier.api.DossierFileResource;
import ru.ilb.filedossier.core.ContentDispositionMode;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.filedossier.usecases.upload.PublishDossierFile;
import ru.ilb.filedossier.filedossier.usecases.upload.UploadDocument;

import javax.inject.Inject;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

public class DossierFileResourceImpl implements DossierFileResource {

    /**
     * Upload document use case.
     */
    @Inject
    private UploadDocument uploadDocument;

    @Inject
    private PublishDossierFile publishDossierFile;

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

    /**
     * Dossier file model.
     */
    private DossierFile dossierFile;

    final void setDossierFile(DossierFile dossierFile) {
        this.dossierFile = dossierFile;
    }

    @Override
    public Response download(ContentDispositionMode mode) {
        final Representation representation = dossierFile.getRepresentation();

        final String contentDisposition = mode == ContentDispositionMode.ATTACHMENT
                ? mode.value() + "; filename=" + representation.getFileName()
                : mode.value();

        return Response.ok(representation.getContents())
                .header("Content-Type", representation.getMediaType())
                .header("Content-Disposition", contentDisposition)
                .build();
    }

    @Override
    public void publish(File file) {
        publishDossierFile.publish(file, dossierFile, getCurrentContextKey());
    }

    @Override
    public void upload(File file) {
        try {
            uploadDocument.upload(file, dossierFile, getCurrentContextKey());
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public DossierContextResource getDossierContextResource() {
        final DossierContextResourceImpl resource = new DossierContextResourceImpl();
        resource.setContextKey(getCurrentContextKey());
        applicationContext.getAutowireCapableBeanFactory().autowireBean(resource);
        return resourceContext.initResource(resource);
    }

    private String getCurrentContextKey() {
        return dossierFile.getParent().getCode() + "/" + dossierFile.getCode();
    }
}
