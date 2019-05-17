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
import java.util.List;

/**
 *
 * @author SPoket
 */
public class DossierImpl implements Dossier {
    
    private List<DossierFile> dossierFiles = new ArrayList<>();
    
    private final String code;
    
    private final String name;
    
    public DossierImpl(String code, String name){
        this.code = code;
        this.name = name;   
    }
    
    public DossierImpl(String code, String name, List<DossierFile> dossierFiles){
        this.code = code;
        this.name = name;
        this.dossierFiles = dossierFiles;
    }
    
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public void addFile(DossierFile file) {
        dossierFiles.add(file);
    }
    
    @Override
    public List<DossierFile> getFiles() {
        return dossierFiles;
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
    
}
