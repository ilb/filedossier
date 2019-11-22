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
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import ru.ilb.filedossier.ddl.reader.DossierReader;
import ru.ilb.filedossier.ddl.reader.XmlDossierReader;
import ru.ilb.filedossier.ddl.reader.XsltDossierReader;
import ru.ilb.filedossier.utils.FSUtils;

/**
 *
 * Файловый репозиторий досье
 *
 * @author slavb
 */
public class FileDossierDefinitionRepository implements DossierDefinitionRepository {

    private final URI dossierModelsBaseUri;

    private final static List<DossierReader> DOSSIER_READERS = Arrays.asList(new XmlDossierReader(), new XsltDossierReader());

    /**
     *
     * @param dossierModelsBaseUri path to dossier models store. all models resolved against this path
     */
    public FileDossierDefinitionRepository(URI dossierModelsBaseUri) {
        this.dossierModelsBaseUri = FSUtils.loadFileSystemProvider(dossierModelsBaseUri);
    }

    private Path getDossierPackageBase(String dossierPackage) {
        return Paths.get(dossierModelsBaseUri).resolve(dossierPackage);
    }

    private Path getDossierDefinitionPath(String dossierPackage, String extension) {
        return getDossierPackageBase(dossierPackage).resolve(dossierPackage + extension);
    }

    private DossierReader getDossierReader(String dossierPackage) {
        return DOSSIER_READERS.stream()
                .filter(dr -> Files.exists(getDossierDefinitionPath(dossierPackage, dr.modelFileExtension())))
                .findFirst()
                .orElseThrow(() -> new DossierPackageNotFoundException(dossierPackage));
    }

    @Override
    public PackageDefinition getDossierPackage(String dossierPackage, String dossierMode) {

        try {
            DossierReader dossierReader = getDossierReader(dossierPackage);
            Path dossierPath = getDossierDefinitionPath(dossierPackage, dossierReader.modelFileExtension());
            String contents = new String(Files.readAllBytes(dossierPath));
            PackageDefinition dossierPackageDefinition = dossierReader.read(contents);
            dossierPackageDefinition.setBaseUri(dossierPath.toUri());
            return dossierPackageDefinition;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
