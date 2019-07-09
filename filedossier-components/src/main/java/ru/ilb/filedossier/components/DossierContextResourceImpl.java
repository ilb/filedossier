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

import org.apache.cxf.jaxrs.json.basic.JsonMapObject;
import ru.ilb.filedossier.api.DossierContextResource;
import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.entities.DossierFile;


public class DossierContextResourceImpl implements DossierContextResource {

    private final DossierFile dossierFile;

    public DossierContextResourceImpl(DossierFile dossierFile) {
        this.dossierFile = dossierFile;
    }


    @Override
    public JsonMapObject getContext() {
        return new JsonMapObject(dossierFile.getDossierContext().asMap());
    }

    @Override
    public void setContext(JsonMapObject jsonmapobject) {
        dossierFile.setDossierContext(new DossierContextImpl(jsonmapobject.asMap()));
    }

}
