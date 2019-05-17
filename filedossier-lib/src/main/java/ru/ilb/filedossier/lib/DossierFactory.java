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
package ru.ilb.filedossier.lib;

import java.util.ArrayList;
import java.util.List;
import ru.ilb.filedossier.model.DossierModel;
import ru.ilb.filedossier.model.DossierModelFile;
import ru.ilb.filestorage.Store;

/**
 *
 * @author slavb
 */
public class DossierFactory {

    private final Store store;
    
    private DossierFactory(Store store){
        this.store = store;
    }
       
    public static DossierFactory newInstance(Store store){
        return new DossierFactory(store);   
    }
    
    public Dossier fromModel(DossierModel dossierModel) {
        String code = dossierModel.getCode();
        String name = dossierModel.getName();
        List<DossierModelFile> modelFiles = dossierModel.getDossierModelFiles();
        
        List<DossierFile> dossierFiles = new ArrayList<>();
        modelFiles.forEach((modelFile) -> {
            dossierFiles.add(new DossierFileImpl(
                    store, modelFile.getCode(), modelFile.getName(),
                    modelFile.getRequired(), modelFile.getReadonly(),
                    modelFile.getVisible()
            ));
        });
        return new DossierImpl(code, name, dossierFiles);
    }

}