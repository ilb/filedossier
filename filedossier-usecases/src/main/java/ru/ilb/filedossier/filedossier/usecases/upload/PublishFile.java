/*
 * Copyright 2019 kuznetsov_me.
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
package ru.ilb.filedossier.filedossier.usecases.upload;

import ru.ilb.filedossier.document.merger.DocumentMergerFactory;
import ru.ilb.filedossier.document.merger.functions.DocumentMerger;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.DossierFileVersion;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author kuznetsov_me
 */
@Named
public class PublishFile {

    public void publish(File file, DossierFile dossierFile) {
        DossierFileVersion version = dossierFile.getLatestVersion();

        if (version == null) {
            throw new RuntimeException("dossier file isn't created");
        }

        try {
            byte[] mergedFile = mergeFiles(version, file);
            version.setMediaType(MimeTypeUtil.guessMimeTypeFromByteArray(mergedFile));
            version.setContents(mergedFile);
        } catch (IOException e) {
            throw new RuntimeException("Error while saving: " + e);
        }
    }

    private byte[] mergeFiles(DossierFileVersion dossierFile, File file) throws IOException {
        DocumentMergerFactory factory = DocumentMergerFactory.getInstance();

        String dossierFileMediaType = dossierFile.getMediaType();
        String newFileMediaType = MimeTypeUtil.guessMimeTypeFromFile(file);

        DocumentMerger merger = factory.getDocumentMerger(dossierFileMediaType, newFileMediaType);
        return merger.apply(
                dossierFile.getContents(),
                Files.readAllBytes(file.toPath()));
    }
}
