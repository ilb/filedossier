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

import ru.ilb.filedossier.document.merger.DocumentMergerExecutor;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.DossierFileVersion;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author kuznetsov_me
 */
@Named
public class PublishFile {

    private PublishFileNewVersion publishNewVersion;
    private DocumentMergerExecutor executor;

    @Inject
    public PublishFile(PublishFileNewVersion publishNewVersion) {
        this.publishNewVersion = publishNewVersion;
    }


    public void publish(File file, DossierFile dossierFile) {
        executor = DocumentMergerExecutor.getInstance();
        DossierFileVersion version = dossierFile.getLatestVersion();

        // if dossier file isn't created create new
        if (version == null) {
            publishNewVersion.publish(file, dossierFile);
            return;
        }

        try {
            executor.addDocumentToMerge(file);
            executor.addDocumentToMerge(version.getContents());
            byte[] mergedDocument = executor.executeMerge();
            version.setMediaType(MimeTypeUtil.guessMimeTypeFromByteArray(mergedDocument));
            version.setContents(mergedDocument);
        } catch (IOException e) {
            throw new RuntimeException("Error while merging current dossier file with new file: " + e);
        }
    }

    public void mergeAndPublish(List<File> files, DossierFile dossierFile) {
        executor = DocumentMergerExecutor.fromList(files);
        DossierFileVersion version = dossierFile.getLatestVersion();

        // if dossier file isn't created create new
        if (version == null) {
            publishNewVersion.mergeAndPublish(files, dossierFile);
            return;
        }

        try {
            executor.addDocumentToMerge(version.getContents());
            byte[] mergedDocument = executor.executeMerge();
            version.setMediaType(MimeTypeUtil.guessMimeTypeFromByteArray(mergedDocument));
            version.setContents(mergedDocument);
        } catch (IOException e) {
            throw new RuntimeException("Error while merging current dossier with new files: " + e);
        }
    }
}
