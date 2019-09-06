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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import ru.ilb.filedossier.api.DossierContextResource;
import ru.ilb.filedossier.api.DossierFileResource;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.filedossier.usecases.upload.UploadDocument;

@Named
public class DossierFileResourceImpl implements DossierFileResource {

    @Inject
    private DossierContextResourceImpl resource;

    @Inject
    private UploadDocument uploadDocument;

    private DossierFile dossierFile;

    @Override
    public Response download() {
        Representation representation = dossierFile.getRepresentation();
        return Response.ok(representation.getContents())
                .header("Content-Type", representation.getMediaType())
                .header("Content-Disposition attachment;",
                        "filename=" + representation.getFileName())
                .build();
    }

    @Override
    public void publish(InputStream inputstream) {
        try {
            byte[] data = Util.toByteArray(inputstream);
            dossierFile.setContents(data);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read input stream: " + e);
        }
    }

    @Override
    public void upload(File file) {
        try {
            uploadDocument.upload(file, dossierFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DossierContextResource getDossierContextResource() {
        String fileContextKey = dossierFile.getParent()
                .getCode() + "/" + dossierFile.getCode();
        resource.setContextKey(fileContextKey);
        return resource;
    }

    public void setDossierFile(DossierFile dossierFile) {
        this.dossierFile = dossierFile;
    }
}
