/*
 * Copyright 2019 develop01.
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ru.ilb.filedossier.context.DossierContextService;
import ru.ilb.filedossier.entities.Dossier;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.DossierPath;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.entities.Store;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;
import ru.ilb.filedossier.representation.IdentityRepresentation;

/**
 *
 * @author SPoket
 */
public class DossierFileImpl implements DossierFile {

    protected DossierContext context;

    protected Dossier parent;

    protected Store store;

    protected final String code;

    protected final String name;

    protected final boolean required;

    protected final boolean readonly;

    protected final boolean hidden;

    protected final String mediaType;

    protected final String extension;

    protected final Map<String, Representation> representationsMap;

    protected final Representation representation;

    protected final DossierContextService dossierContextService;

    public DossierFileImpl(Store store, String code, String name, boolean required,
            boolean readonly, boolean hidden, String mediaType,
            List<Representation> representations,
            DossierContextService dossierContextService) {
        this.store = store;
        this.code = code;
        this.name = name;
        this.required = required;
        this.readonly = readonly;
        this.hidden = hidden;
        this.mediaType = mediaType;
        this.extension = MimeTypeUtil.getExtension(mediaType);
        this.representationsMap = representations.stream().peek(r -> r.setParent(this))
                .collect(Collectors.toMap(r -> r.getMediaType(), r -> r));
        this.representation = representations.isEmpty() ? new IdentityRepresentation(mediaType)
                : representations.iterator().next();
        this.representation.setParent(this);
        this.dossierContextService = dossierContextService;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    protected String getStoreFileName() {
        return extension == null ? code : code + "." + extension;
    }

    @Override
    public boolean getRequired() {
        return required;
    }

    @Override
    public boolean getReadonly() {
        return readonly;
    }

    @Override
    public boolean getHidden() {
        return hidden;
    }

    @Override
    public boolean getExists() {
        return store.isExist(getStoreFileName());
    }

    @Override
    public byte[] getContents() {
        try {
            return store.getContents(getStoreFileName());
        } catch (NoSuchFileException ex) {
            throw new FileNotExistsException(getStoreFileName());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setContents(byte[] data) {
        try {
            store.setContents(getStoreFileName(), data);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public Representation getRepresentation() {
        return representation;
    }

    @Override
    public Dossier getParent() {
        return parent;
    }

    @Override
    public void setParent(DossierPath parent) {
        assert Dossier.class
                .isAssignableFrom(parent.getClass()) : "Dossier instance should be passed as argument instead of "
                + parent.getClass().getCanonicalName();
        this.parent = (Dossier) parent;
    }

    @Override
    public DossierContext getContext() {
        if (this.context == null) {
            this.context = dossierContextService.getContext(getContextCode());
        }
        return this.context;
    }

    @Override
    public void setDossierContext(DossierContext dossierContext) {
        dossierContextService.putContext(getContextCode(), dossierContext);
    }

    protected String getContextCode() {
        return parent.getCode() + "/" + code;
    }

    @Override
    public void setContents(File contentsFile) {
        try {
            setContents(Files.readAllBytes(contentsFile.toPath()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean isValid() {
        return getExists();
    }
}
