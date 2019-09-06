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
package ru.ilb.filedossier.representation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import ru.ilb.filedossier.entities.DossierContents;
import ru.ilb.filedossier.entities.DossierPath;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.entities.RepresentationPart;
import ru.ilb.filedossier.entities.Store;
import ru.ilb.filedossier.representation.delegate.MultipartDelegate;
import ru.ilb.filedossier.representation.delegate.PDFMultipartDelegate;

/**
 * Represents raw file contents
 *
 * @author slavb
 */
public class IdentityRepresentation implements Representation {

    protected DossierContents parent;

    protected Store store;

    protected final String mediaType;

    protected MultipartDelegate multipartDelegate;

    public IdentityRepresentation(Store store, String mediaType) {
        this.mediaType = mediaType;
        this.store = store;
    }

    @Override
    public String getCode() {
        return parent.getCode();
    }

    @Override
    public String getName() {
        return parent.getName();
    }

    @Override
    public void setParent(DossierPath parent) {
        assert DossierContents.class.isAssignableFrom(
                parent.getClass()) : "DossierContents instance should be passed as argument instead of "
                + parent.getClass().getCanonicalName();
        this.parent = (DossierContents) parent;
        initMultipartDelegate();
    }

    @Override
    public DossierContents getParent() {
        return parent;
    }

    @Override
    public byte[] getContents() {
       try {
            return multipartDelegate.isEmpty()
                   ? store.isExist(getFileName())
                   ? store.getContents(getFileName()) : generateRepresentation()
                   : multipartDelegate.getContents();
        } catch (IOException e) {
            throw new RuntimeException("Error getting representation: " + e);
        }
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public void setContents(byte[] contents) {
        try {
            store.setContents(getFileName(), contents);
        } catch (IOException e) {
            throw new RuntimeException("Error while saving representation: " + e);
        }
    }

    @Override
    public void setContents(File file) {
        byte[] contents;
        try {
            contents = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("File not exist: " + e);
        }
        try {
            store.setContents(getFileName(), contents);
        } catch (IOException e) {
            throw new RuntimeException("Error while saving representation: " + e);
        }
    }

    @Override
    public byte[] generateRepresentation() {
        return getContents();
    }

    @Override
    public String getExtension() {
        return parent.getExtension();
    }

    @Override
    public void setRepresentationPart(RepresentationPart part) {
        multipartDelegate.setRepresentationPart(part);
    }

    private void initMultipartDelegate() {
        if (multipartDelegate == null) {
            Store nestedStore = store.getNestedFileStore(getCode());
            multipartDelegate = new PDFMultipartDelegate(nestedStore);
        }
    }
}
