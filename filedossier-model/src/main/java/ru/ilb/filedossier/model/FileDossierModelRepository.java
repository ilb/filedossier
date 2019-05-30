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
package ru.ilb.filedossier.model;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

/**
 *
 * Файловый репозиторий досье
 *
 * @author slavb
 */
public class FileDossierModelRepository implements DossierModelRepository {

    private static final String URI_2001_SCHEMA_XSD = "http://www.w3.org/2001/XMLSchema";
    private static final String MODEL_SCHEMA_XSD_PATH = "schemas/filedossier/model.xsd";
    private final SchemaFactory schemaFactory = SchemaFactory.newInstance(URI_2001_SCHEMA_XSD);
    private final Schema schema;

    final JAXBContext jaxbContext;

    private final URI dossierModelsPath;

    private String modelFileExtension = ".xml";

    public FileDossierModelRepository(URI dossierModelsPath) {

        this.dossierModelsPath = dossierModelsPath;
        try {
            jaxbContext = JAXBContext.newInstance("ru.ilb.filedossier.model");
            schema = schemaFactory.newSchema(getClass().getClassLoader().getResource(MODEL_SCHEMA_XSD_PATH));
        } catch (JAXBException | SAXException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Path getDossierModelPath(String dossierPackage) {
        return Paths.get(dossierModelsPath).resolve(Paths.get(dossierPackage + modelFileExtension));
    }

    @Override
    public URI getDossierModelUri(String dossierPackage) {
        return getDossierModelPath(dossierPackage).toUri();
    }

    @Override
    public DossierModel getDossierModel(String dossierPackage, String dossierCode) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            PackageModel dossierPackageModel = (PackageModel) unmarshaller.unmarshal(getDossierModelUri(dossierPackage).toURL());
            return dossierPackageModel.getDossiers().stream()
                    .filter(d -> d.getCode().equals(dossierCode)).findFirst().orElseThrow(() -> new DossierNotFoundException(dossierCode));
        } catch (JAXBException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
