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
import javax.ws.rs.Path;
import ru.ilb.filedossier.api.DossierResource;
import ru.ilb.filedossier.api.DossiersResource;
import ru.ilb.filedossier.lib.DossierFactory;
import ru.ilb.filedossier.mappers.DossierMapper;

@Named
@Path("dossiers")
public class DossiersResourceImpl implements DossiersResource {

    @Inject
    private DossierFactory dossierFactory;

    @Inject
    private DossierMapper dossierMapper;

    @Override
    public DossierResource getDossierResource(String dossierKey,  String dossierPackage, String dossierCode) {
        return new DossierResourceImpl(dossierKey, dossierPackage, dossierCode, dossierFactory, dossierMapper);
    }

}
