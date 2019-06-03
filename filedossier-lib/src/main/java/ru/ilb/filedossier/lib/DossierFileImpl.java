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
package ru.ilb.filedossier.lib;

import ru.ilb.filedossier.entities.DossierFile;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;
import ru.ilb.filedossier.representation.IdentityRepresentation;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.store.Store;

/**
 *
 * @author SPoket
 */
public class DossierFileImpl implements DossierFile {

    private final Store storage;

    private final String code;

    private final String name;

    private final boolean required;

    private final boolean readonly;

    private final boolean hidden;

    private final String mediaType;

    private final String extension;

    private final Map<String, Representation> representationsMap;

    private final Representation representation;

    public DossierFileImpl(Store storage, String code, String name,
            boolean required, boolean readonly, boolean hidden, String mediaType,
            List<Representation> representations) {
        this.storage = storage;
        this.code = code;
        this.name = name;
        this.required = required;
        this.readonly = readonly;
        this.hidden = hidden;
        this.mediaType = mediaType;
        this.extension = MimeTypeUtil.getExtension(mediaType);
        this.representationsMap = representations.stream()
                .peek(r -> r.setParent(this))
                .collect(Collectors.toMap(r -> r.getMediaType(), r -> r));
        this.representation = representations.isEmpty() ? new IdentityRepresentation(this, mediaType) : representations.iterator().next();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFileName() {
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
        return storage.isExist(getFileName());
    }

    @Override
    public byte[] getContents() {
        try {
            return storage.getContents(getFileName());
        } catch (NoSuchFileException ex) {
            throw new FileNotExistsException(getFileName());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setContents(byte[] data) {
        try {
            storage.setContents(getFileName(), data);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public String getMediaType() {
        return representation.getMediaType();
    }

    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public Representation getRepresentation() {
        return representation;
    }

}
