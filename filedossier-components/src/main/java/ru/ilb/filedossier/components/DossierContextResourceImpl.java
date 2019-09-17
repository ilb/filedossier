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
import org.apache.cxf.jaxrs.json.basic.JsonMapObject;
import ru.ilb.filedossier.api.DossierContextResource;
import ru.ilb.filedossier.filedossier.usecases.download.DownloadDossierFileContext;
import ru.ilb.filedossier.filedossier.usecases.upload.UploadDossierFileContext;

/**
 * Resource for work with dossier file context.
 */
public class DossierContextResourceImpl implements DossierContextResource {

    /**
     * Use case of download context from the requested dossier file.
     */
    @Inject
    private DownloadDossierFileContext downloadDossierFileContext;

    /**
     * Use case of upload context to the requested dossier file.
     */
    @Inject
    private UploadDossierFileContext uploadDossierFileContext;

    /**
     * Key of context.
     */
    private String contextKey;

    final void setContextKey(String contextKey) {
        this.contextKey = contextKey;
    }

    @Override
    public JsonMapObject getContext() {
        return downloadDossierFileContext.download(contextKey);
    }

    @Override
    public void setContext(JsonMapObject jsonmapobject) {
        uploadDossierFileContext.upload(contextKey, jsonmapobject);
    }
}
