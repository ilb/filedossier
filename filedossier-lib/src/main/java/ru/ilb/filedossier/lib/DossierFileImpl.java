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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import ru.ilb.filestorage.Store;

/**
 *
 * @author SPoket
 */
public class DossierFileImpl implements DossierFile {
    
    private Store storage;
    
    private String code;
    
    private String name;
    
    private boolean exist;
    
    private boolean readOnly;
    
    
    public DossierFileImpl(Store storage, String code, String name){
        this.storage = storage;
        this.code = code;
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isExist() { // TODO
        return exist;
    }

    public boolean isReadOnly() { // TODO
        return readOnly;
    }

    @Override
    public byte[] getContents() throws IOException {
        return storage.getContents(code);
    }

    @Override
    public void putContents(byte[] data) throws IOException {
        storage.putContents(code, data);
    }
    
}
