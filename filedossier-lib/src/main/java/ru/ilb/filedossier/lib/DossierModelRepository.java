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

import java.nio.file.Paths;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import ru.ilb.filedossier.model.DossierModel;

/**
 *
 * @author slavb
 */
public class DossierModelRepository {

    final JAXBContext jaxbContext;

    private final String dossierModelsPath;

    public DossierModelRepository(String dossierModelsPath) {
        this.dossierModelsPath = dossierModelsPath;
        try {
            jaxbContext = JAXBContext.newInstance("ru.ilb.filedossier.model");
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }

    }

    public DossierModel getDossierModel(String dossierCode) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (DossierModel) unmarshaller.unmarshal(Paths.get(dossierModelsPath, dossierCode+".xml").toFile());
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

}
