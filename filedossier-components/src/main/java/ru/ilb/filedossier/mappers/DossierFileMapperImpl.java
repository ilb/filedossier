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
package ru.ilb.filedossier.mappers;

import ru.ilb.filedossier.core.ContentDispositionMode;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.DossierFileVersion;
import ru.ilb.filedossier.view.DossierFileView;

import javax.inject.Named;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Named
public class DossierFileMapperImpl implements DossierFileMapper {

    @Override
    public DossierFileView map(DossierFile model, URI dossierFileResourceUri) {

        DossierFileView df = new DossierFileView();
        df.setCode(model.getCode());
        df.setName(model.getName());
        df.setExists(model.getExists());
        df.setReadonly(model.getReadonly());
        df.setRequired(model.getRequired());
        df.setHidden(model.getHidden());
        df.setAllowedMultiple(model.getAllowedMultiple());
        df.setAllowedMediaTypes(model.getAllowedMediaTypes());
        df.setLinks(buildDossierFileLinks(dossierFileResourceUri));

        if (model.getExists()) {
            DossierFileVersion latestVersion = model.getLatestVersion();
            df.setVersion(String.valueOf(model.getVersionsCount()));
            df.setMediaType(latestVersion.getMediaType());
            df.setLastModified(model.lastModified());
        }

        return df;
    }

    /**
     * Builds various links for different content disposition modes
     *
     * @return list of marshalled links
     * @see javax.ws.rs.core.Link
     */
    private List<Link> buildDossierFileLinks(URI dossierFileResourceUri) {
        List<Link> links = new ArrayList<>();
        Stream.of(ContentDispositionMode.values())
                .forEach(mode -> {
                    Link link = Link
                            .fromUri(addParamToUri(dossierFileResourceUri, "mode", mode.value()))
                            .rel(mode.value())
                            .build();
                    links.add(link);
                });
        Link update = Link
                .fromUri(UriBuilder.fromUri(dossierFileResourceUri).path("update").build())
                .rel("update")
                .build();
        links.add(update);

        Link publish = Link
                .fromUri(UriBuilder.fromUri(dossierFileResourceUri).path("publish").build())
                .rel("publish")
                .build();
        links.add(publish);


        Link context = Link
                .fromUri(UriBuilder.fromUri(dossierFileResourceUri).path("context").build())
                .rel("context")
                .build();
        links.add(context);
        return links;
    }

    private URI addParamToUri(URI uri, String name, Object... params) {
        return UriBuilder.fromUri(uri).queryParam(name, params).build();
    }
}
