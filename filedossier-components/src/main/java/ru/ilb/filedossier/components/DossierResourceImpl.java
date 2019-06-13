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

import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import ru.ilb.filedossier.api.DossierResource;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.core.DossierFactory;
import ru.ilb.filedossier.mappers.DossierMapper;
import ru.ilb.filedossier.view.DossierView;

public class DossierResourceImpl implements DossierResource {

    private final String dossierKey;

    private final String dossierCode;

    private final DossierFactory dossierFactory;

    private final ru.ilb.filedossier.entities.Dossier dossier;

    private final DossierMapper dossierMapper;

    public DossierResourceImpl(String dossierKey, String dossierPackage, String dossierCode, DossierFactory dossierFactory, DossierMapper dossierMapper) {
        this.dossierKey = dossierKey;
        this.dossierCode = dossierCode;
        this.dossierFactory = dossierFactory;
        this.dossier = dossierFactory.getDossier(dossierKey, dossierPackage, dossierCode);
        this.dossierMapper = dossierMapper;
    }

    @Override
    public DossierView getDossier() {
        return dossierMapper.fromModel(dossier);
    }

    @Override
    public Response getContents(String fileCode) {
        Representation representation = this.dossier.getDossierFile(fileCode).getRepresentation();
        return Response.ok(representation.getContents())
                .header("Content-Type", representation.getMediaType())
                .header("Content-Disposition", "attachment; filename=" + representation.getFileName()).build();

    }

    @Override
    public void setContents(String fileCode, InputStream inputstream) {
        try {
            this.dossier.getDossierFile(fileCode).setContents(Util.toByteArray(inputstream));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @POST
    @Consumes("multipart/form-data")
    @Path("/dossierfiles/{fileCode}")
    public void uploadContents(@PathParam("fileCode") String fileCode, MultipartBody body) {
        this.dossier.getDossierFile(fileCode).setContents(body.getRootAttachment().getObject(byte[].class));
    }

}
