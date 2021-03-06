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
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.springframework.context.ApplicationContext;

import ru.ilb.filedossier.api.DossierContextResource;
import ru.ilb.filedossier.api.DossierFileResource;
import ru.ilb.filedossier.contentnegotiation.sevice.ContentNegotiationSevice;
import ru.ilb.filedossier.core.ContentDispositionMode;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.DossierFileVersion;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.exceptions.NotAcceptableMediaType;
import ru.ilb.filedossier.filedossier.usecases.upload.PublishFile;
import ru.ilb.filedossier.filedossier.usecases.upload.PublishFileNewVersion;
import ru.ilb.uriaccessor.URIStorageFactory;

public class DossierFileResourceImpl implements DossierFileResource {

    /**
     * Publish file use case.
     */
    @Inject
    private PublishFile publishFile;

    // TODO: add flag NEW_VERSION to PublishFile and remove PublishFileNewVersion
    /**
     * Publish new version of file use case.
     */
    @Inject
    private PublishFileNewVersion publishFileNewVersion;

    /**
     * Spring application context.
     */
    @Inject
    private ApplicationContext applicationContext;

    /**
     * JAX-RS resources context.
     */
    @Context
    private ResourceContext resourceContext;

    @Inject
    private ContentNegotiationSevice contentNegotiationSevice;

    private final URIStorageFactory uriStorageFactory = new URIStorageFactory();

    /**
     * CXF MessageContext
     */
//    @Context
//    protected MessageContext messageContext;
    /**
     * Dossier file model.
     */
    private DossierFile dossierFile;

    final void setDossierFile(DossierFile dossierFile) {
        this.dossierFile = dossierFile;
    }

    @Override
    public Response download(Integer version, ContentDispositionMode mode, String accept) {

        DossierFileVersion dossierFileVersion;
        if (version == null) {
            dossierFileVersion = dossierFile.getLatestVersion();
        } else {
            dossierFileVersion = dossierFile.getVersion(version);
        }
        if (dossierFileVersion == null) {
            return null;
        }
        //final Message message = PhaseInterceptorChain.getCurrentMessage();
        //List<MediaType> acceptableMediaTypes = messageContext.getHttpHeaders().getAcceptableMediaTypes();
        // text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
        List<String> allowedMediaTypes = dossierFileVersion.getAllowedMediaTypes();
        String acceptableMediaType = contentNegotiationSevice.getAcceptableMediaType(accept, allowedMediaTypes)
                .orElseThrow(() -> new NotAcceptableMediaType(allowedMediaTypes));
        Representation representation = dossierFileVersion.getRepresentation(acceptableMediaType);
        final String contentDisposition = ContentDispositionMode.ATTACHMENT.equals(mode)
                ? mode.value() + "; filename=" + representation.getFileName()
                : mode.value();

        try {
            byte[] contents = representation.getContents();
            return Response.ok(contents)
                    .header("Content-Size", contents.length)
                    .header("Content-Type", representation.getMediaType())
                    .header("Content-Disposition", contentDisposition)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("can not load representation: " + e);
        }
    }

    @Override
    public void update(MultipartBody body) {
        if (body.getAllAttachments().isEmpty()) {
            throw new WebApplicationException("Upload empty");
        }

        if (body.getAllAttachments().size() < 1) {
            publishFile.publish(body.getRootAttachment().getObject(File.class), dossierFile);
        } else {
            publishFile.mergeAndPublish(body.getAllAttachments().stream()
                    .map(att -> att.getObject(File.class))
                    .collect(Collectors.toList()),
                    dossierFile);
        }
        //dossierFile.lastModified() should be updated?

    }

    @Override
    public void publish(MultipartBody body) {
        if (body.getAllAttachments().isEmpty()) {
            throw new WebApplicationException("Upload empty");
        }

        if (body.getAllAttachments().size() < 1) {
            publishFileNewVersion.publish(body.getRootAttachment().getObject(File.class), dossierFile);
        } else {
            publishFileNewVersion.mergeAndPublish(body.getAllAttachments().stream()
                    .map(att -> att.getObject(File.class))
                    .collect(Collectors.toList()),
                    dossierFile);
        }
    }

    @Override
    public DossierContextResource getDossierContextResource() {
        final DossierContextResourceImpl resource = new DossierContextResourceImpl();
        resource.setContextKey(dossierFile.getContextKey());
        applicationContext.getAutowireCapableBeanFactory().autowireBean(resource);
        return resourceContext.initResource(resource);
    }

    @Override
    public Response container(String path) {
        StringBuilder redirect = new StringBuilder(100);
        redirect.append("containers/");
        redirect.append(uriStorageFactory.getURIStorage().registerUri(dossierFile.getLatestVersion().getFilePath().toUri(), dossierFile.getLatestVersion().getMediaType()));
        if (path != null && !path.isEmpty()) {
            if (!path.startsWith("/")) {
                redirect.append('/');
            }
            redirect.append(path);
        }
        return Response.seeOther(URI.create(redirect.toString())).build();
    }
}
