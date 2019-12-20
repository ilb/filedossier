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

import ru.ilb.filedossier.entities.*;
import ru.ilb.filedossier.entities.DossierFileVersion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author SPoket
 */
public class DossierFileImpl implements DossierFile {

    private Dossier parent;

    private Store store;

    private final String code;

    protected final String name;

    protected final boolean required;

    private final boolean readonly;

    private final boolean hidden;

    private final boolean allowedMultiple;

    private List<DossierFileVersion> versions;

    private Map<String, DossierFileVariation> variations;

    DossierFileImpl(Store store, String code, String name,
                    boolean required, boolean readonly,
                    boolean hidden, boolean allowedMultiple,
                    List<DossierFileVersion> versions,
                    Map<String, DossierFileVariation> variations) {
        this.store = store;
        this.code = code;
        this.name = name;
        this.required = required;
        this.readonly = readonly;
        this.hidden = hidden;
        this.allowedMultiple = allowedMultiple;
        this.variations = variations;
        this.versions = versions.stream()
                .peek(version -> version.setParent(this))
                .collect(Collectors.toList());
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
    public boolean getAllowedMultiple() {
        return allowedMultiple;
    }

    @Override
    public boolean getExists() {
        return store.getObjectsCount() > 0;
    }

    @Override
    public String getContextKey() {
        return String.format("%s/%s", parent.getContextKey(), getCode());
    }

    @Override
    public String lastModified() {
        Long millis =  store.lastModified(String.valueOf(versions.size() - 1));
        Date date = new Date(millis);
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date);
    }

    @Override
    public Integer getVersionsCount() {
        return store.getObjectsCount();
    }

    @Override
    public List<String> getAllowedMediaTypes() {
        List<String> allowedMediaTypes = new ArrayList<>();
        variations.forEach((mt, v) -> allowedMediaTypes.add(mt));
        return allowedMediaTypes;
    }

    @Override
    public DossierFileVersion createNewVersion(String mediaType) {

        if (getReadonly()){
            throw new IllegalArgumentException("Dossier file is readonly: " + getCode());
        }

        if (!variations.containsKey(mediaType)) {
           throw new IllegalArgumentException("Specified media type '" + mediaType +"' is not allowed");
        }

        DossierFileVariation variation = variations.get(mediaType);
        DossierFileVersion newVersion = new ConcreteDossierFileVersion(
                variation.getMediaType(),
                variation.getRepresentations());
        Store newVersionStore = store.getNestedFileStore(String.valueOf(versions.size()));

        newVersion.setStore(newVersionStore);
        newVersion.setParent(this);
        return newVersion;
    }

    @Override
    public DossierFileVersion getVersion(int version) {
        return versions.get(version - 1);
    }

    @Override
    public DossierFileVersion getLatestVersion() {
        if (versions.size() > 0) {
            return versions.get(versions.size() - 1);
        } else {
            return null;
        }
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
}
