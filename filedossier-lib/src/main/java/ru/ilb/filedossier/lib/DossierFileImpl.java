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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.ilb.filestorage.Store;

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

    private final boolean visible;

    private final boolean exists;

    public DossierFileImpl(Store storage, String code, String name,
            boolean required, boolean readonly, boolean visible, boolean exists) {
        this.storage = storage;
        this.code = code;
        this.name = name;
        this.required = required;
        this.readonly = readonly;
        this.visible = visible;
        this.exists = exists;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isReadOnly() {
        return readonly;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public byte[] getContents() throws IOException{
        return storage.getContents(code);
    }

    @Override
    public void putContents(byte[] data) throws IOException{
        storage.putContents(code, data);
    }

}
