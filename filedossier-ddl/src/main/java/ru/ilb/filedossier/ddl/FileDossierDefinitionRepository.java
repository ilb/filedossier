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
import java.nio.file.Files;
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
import ru.ilb.filedossier.ddl.DossierDefinition;
import ru.ilb.filedossier.ddl.DossierDefinitionRepository;
import ru.ilb.filedossier.ddl.DossierNotFoundException;
import ru.ilb.filedossier.ddl.PackageDefinition;
import ru.ilb.filedossier.ddl.PackageDefinition;
import ru.ilb.filedossier.ddl.reader.XmlDossierReader;
import ru.ilb.filedossier.utils.FSUtils;

/**
 *
 * Файловый репозиторий досье
 *
 * @author slavb
 */
public class FileDossierDefinitionRepository implements DossierDefinitionRepository {

    private final URI dossierModelsBaseUri;

    private final XmlDossierReader xmlDossierReader = new XmlDossierReader();

    /**
     *
     * @param dossierModelsBaseUri path to dossier models store. all models resolved against this path
     */
    public FileDossierDefinitionRepository(URI dossierModelsBaseUri) {
        this.dossierModelsBaseUri = FSUtils.loadFileSystemProvider(dossierModelsBaseUri);
    }

    private Path getDossierDefinitionPath(String dossierPackage) {
        return Paths.get(dossierModelsBaseUri).resolve(dossierPackage).resolve(dossierPackage + XmlDossierReader.MODEL_FILE_EXTENSION);
    }

    @Override
    public URI getDossierDefinitionUri(String dossierPackage) {
        return getDossierDefinitionPath(dossierPackage).toUri();
    }

    @Override
    public DossierDefinition getDossierDefinition(String dossierPackage, String dossierCode, String dossierMode) {

        try {
            String contents = new String(Files.readAllBytes(getDossierDefinitionPath(dossierPackage)));
            PackageDefinition dossierPackageDefinition = xmlDossierReader.read(contents);
            return dossierPackageDefinition.getDossiers().stream()
                    .filter(d -> d.getCode().equals(dossierCode)).findFirst().orElseThrow(() -> new DossierNotFoundException(dossierCode));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
