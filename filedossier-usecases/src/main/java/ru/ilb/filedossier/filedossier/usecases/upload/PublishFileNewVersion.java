package ru.ilb.filedossier.filedossier.usecases.upload;

import ru.ilb.filedossier.document.merger.DocumentMergerExecutor;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.DossierFileVersion;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Named
public class PublishFileNewVersion {

    DocumentMergerExecutor mergerExecutor;

    public void publish(File file, DossierFile dossierFile){
        String mediaType = MimeTypeUtil.guessMimeTypeFromFile(file);
        DossierFileVersion version = dossierFile.createNewVersion(mediaType);
        try {
            version.setContents(file);
        } catch (IOException e) {
            throw new RuntimeException("Error while saving new dossier file version");
        }
    };

    public void mergeAndPublish(List<File> files, DossierFile dossierFile) {
        mergerExecutor = DocumentMergerExecutor.fromList(files);
        byte[] publishDocument = mergerExecutor.executeMerge();
        String mediaType = MimeTypeUtil.guessMimeTypeFromByteArray(publishDocument);
        DossierFileVersion newVersion = dossierFile.createNewVersion(mediaType);
        try {
            newVersion.setContents(publishDocument);
        } catch (IOException e) {
            throw new RuntimeException("Error while saving new dossier file version");
        }
    }
}
