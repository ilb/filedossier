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

import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.entities.DossierContents;


/**
 * Represents raw file contents
 * @author slavb
 */
public class IdentityRepresentation implements Representation {

    private DossierContents parent;
    private final String mediaType;

    public IdentityRepresentation(DossierContents parent, String mediaType) {
        this.parent = parent;
        this.mediaType = mediaType;
    }

    @Override
    public void setParent(DossierContents parent) {
        this.parent = parent;
    }

    @Override
    public byte[] getContents() {
        return parent.getContents();
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public void setContents(byte[] data) {
        parent.setContents(data);
    }

    @Override
    public String getFileName() {
        return parent.getFileName();
    }

}
