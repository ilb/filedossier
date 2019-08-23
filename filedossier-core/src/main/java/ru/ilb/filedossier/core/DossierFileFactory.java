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
package ru.ilb.filedossier.core;

import java.util.List;
import ru.ilb.filedossier.context.DossierContextService;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.entities.Store;

/**
 *
 * @author kuznetsov_me
 */
public class DossierFileFactory {

    public static DossierFile createDossierFile(Store store, String code, String name, boolean required,
            boolean readonly, boolean hidden, String mediaType, List<Representation> representations,
            DossierContextService dossierContextService) {

        // TODO: multiple representations
        String defaultRepresentationMediaType = representations.get(0).getMediaType();
        switch (defaultRepresentationMediaType) {
            case "application/pdf":
                return new PdfDossierFile(store, code, name, required, readonly, hidden, mediaType, representations,
                        dossierContextService);
            default:
                return new DossierFileImpl(store, code, name, required, readonly, hidden, mediaType, representations,
                        dossierContextService);
        }
    }
}
