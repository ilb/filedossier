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
package ru.ilb.filedossier.ddl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import ru.ilb.filedossier.ddl.DossierDefinition;
import ru.ilb.filedossier.ddl.PackageDefinition;
import ru.ilb.filedossier.utils.FSUtils;

/**
 *
 * Файловый репозиторий досье
 *
 * @author slavb
 */
public class FileDossierDefinitionRepository implements DossierDefinitionRepository {

    private static final String URI_2001_SCHEMA_XSD = "http://www.w3.org/2001/XMLSchema";
    private static final String MODEL_SCHEMA_XSD_PATH = "schemas/filedossier/ddl.xsd";
    private final SchemaFactory schemaFactory = SchemaFactory.newInstance(URI_2001_SCHEMA_XSD);
    private final Schema schema;

    final JAXBContext jaxbContext;

    private final URI dossierModelsPath;

    private String modelFileExtension = ".xml";

    public FileDossierDefinitionRepository(URI dossierModelsPath) {
        this.dossierModelsPath = FSUtils.loadFileSystemProvider(dossierModelsPath);
        try {
            jaxbContext = JAXBContext.newInstance("ru.ilb.filedossier.ddl");
            schema = schemaFactory.newSchema(getClass().getClassLoader().getResource(MODEL_SCHEMA_XSD_PATH));
        } catch (JAXBException | SAXException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Path getDossierDefinitionPath(String dossierPackage) {
        return Paths.get(dossierModelsPath).resolve(dossierPackage).resolve(dossierPackage + modelFileExtension);
    }

    @Override
    public URI getDossierDefinitionUri(String dossierPackage) {
        return getDossierDefinitionPath(dossierPackage).toUri();
    }

    @Override
    public DossierDefinition getDossierDefinition(String dossierPackage, String dossierCode) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            PackageDefinition dossierPackageDefinition = (PackageDefinition) unmarshaller.unmarshal(getDossierDefinitionUri(dossierPackage).toURL());
            return dossierPackageDefinition.getDossiers().stream()
                    .filter(d -> d.getCode().equals(dossierCode)).findFirst().orElseThrow(() -> new DossierNotFoundException(dossierCode));
        } catch (JAXBException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
