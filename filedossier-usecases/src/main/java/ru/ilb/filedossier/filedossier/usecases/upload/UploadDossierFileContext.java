/*
 * Copyright 2019 kuznetsov_me.
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
package ru.ilb.filedossier.filedossier.usecases.upload;

import javax.inject.Inject;
import javax.inject.Named;
import org.apache.cxf.jaxrs.json.basic.JsonMapObject;
import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.context.DossierContextService;
import ru.ilb.filedossier.entities.DossierContext;

/**
 *
 * @author kuznetsov_me
 */
@Named
public class UploadDossierFileContext {

    @Inject
    private DossierContextService contextService;

    public void upload(String contextKey, JsonMapObject context) {
        DossierContext dossierContext = new DossierContextImpl(context.asMap());
        contextService.putContext(contextKey, dossierContext);
    }
}
