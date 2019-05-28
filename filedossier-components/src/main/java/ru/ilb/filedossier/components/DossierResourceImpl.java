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
import javax.ws.rs.core.Response;
import ru.ilb.filedossier.api.DossierResource;
import ru.ilb.filedossier.lib.DossierFactory;
import ru.ilb.filedossier.mappers.DossierMapper;
import ru.ilb.filedossier.view.DossierView;

public class DossierResourceImpl implements DossierResource {

    private final String dossierKey;

    private final String dossierCode;

    private final DossierFactory dossierFactory;

    private final ru.ilb.filedossier.lib.Dossier dossier;

    private final DossierMapper dossierMapper;

    public DossierResourceImpl(String dossierKey, String dossierCode, DossierFactory dossierFactory, DossierMapper dossierMapper) {
        this.dossierKey = dossierKey;
        this.dossierCode = dossierCode;
        this.dossierFactory = dossierFactory;
        this.dossier = dossierFactory.createDossier(dossierKey, dossierCode);
        this.dossierMapper = dossierMapper;
    }

    @Override
    public DossierView getDossier() {
        return dossierMapper.fromModel(dossier);
    }

    @Override
    public Response getContents(String fileCode) {
        try {
            byte[] contents = this.dossier.getDossierFile(fileCode).getContents();
            return Response.ok(contents).build();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void putContents(String fileCode, InputStream inputstream) {
        try {
            this.dossier.getDossierFile(fileCode).putContents(Util.toByteArray(inputstream));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
