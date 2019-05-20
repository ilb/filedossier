/*
 * Copyright 2019 SPoket.
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author SPoket
 */
public class DossierImpl implements Dossier {

    private Map<String, DossierFile> dossierFiles = new LinkedHashMap<>();

    private final String code;

    private final String name;

    public DossierImpl(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public DossierImpl(String code, String name, List<DossierFile> dossierFiles) {
        this.code = code;
        this.name = name;
        this.dossierFiles = dossierFiles.stream().collect(Collectors.toMap(df -> df.getCode(), df -> df));
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public void addFile(DossierFile file) {
        dossierFiles.put(file.getCode(), file);
    }

    @Override
    public List<DossierFile> getFiles() {
        return new ArrayList<>(dossierFiles.values());
    }

    @Override
    public List<DossierFile> getContextFiles(DossierContext dossierContext) {
        String contextProperty = "dossierFiles";
        List<DossierFile> contextFiles = new ArrayList<>();

        if (dossierContext.containsProperty(contextProperty)) {
            contextFiles = (List) dossierContext.getProperty(contextProperty);
        }
        return contextFiles;
    }

    @Override
    public DossierFile getFile(String fileCode) {
        DossierFile file = this.dossierFiles.get(fileCode);
        if (file == null) {
            throw new DossierFileNotFoundException(fileCode);
        }
        return file;
    }

}
