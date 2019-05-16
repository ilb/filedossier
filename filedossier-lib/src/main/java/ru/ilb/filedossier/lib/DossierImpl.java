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

import java.util.ArrayList;
import java.util.List;
import ru.ilb.filedossier.lib.DossierFileImpl;
import ru.ilb.filedossier.lib.Dossier;

/**
 *
 * @author develop01
 */
public class DossierImpl implements Dossier {
    
    List<DossierFile> dossierFiles = new ArrayList<>();
    
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
