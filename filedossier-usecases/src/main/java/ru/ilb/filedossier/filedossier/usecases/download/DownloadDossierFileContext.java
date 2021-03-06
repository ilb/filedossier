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
package ru.ilb.filedossier.filedossier.usecases.download;

import javax.inject.Inject;
import javax.inject.Named;
import org.apache.cxf.jaxrs.json.basic.JsonMapObject;
import ru.ilb.filedossier.context.DossierContextService;

/**
 *
 * @author kuznetsov_me
 */
@Named
public class DownloadDossierFileContext {

    @Inject
    private DossierContextService dossierContextService;

    public JsonMapObject download(String contextKey) {
        return new JsonMapObject(dossierContextService
                .getContext(contextKey)
                .asMap());
    }
}
