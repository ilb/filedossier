package ru.ilb.filedossier.filedossier.usecases.upload;

import ru.ilb.filedossier.context.DossierContextService;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.DossierFileVersion;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;

@Named
public class PublishFileNewVersion {

    public void publish(File file, DossierFile dossierFile){
        String mediaType = MimeTypeUtil.guessMimeTypeFromFile(file);
        DossierFileVersion version = dossierFile.createNewVersion(mediaType);
        try {
            version.setContents(file);
        } catch (IOException e) {
            throw new RuntimeException("error while saving new dossier file version");
        }
    };
}
