package ru.ilb.filedossier.filedossier.usecases.upload;

import ru.ilb.filedossier.context.DossierContextService;
import ru.ilb.filedossier.entities.DossierFile;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

@Named
public class PublishDossierFile extends UploadUseCase{

    private final DossierContextService contextService;

    @Inject
    public PublishDossierFile(DossierContextService contextService) {
        super(contextService);
        this.contextService = contextService;
    }

    public void publish(File file, DossierFile dossierFile, String contextKey) {
        dossierFile.setContents(file);
        setUploadTime(contextKey);
    }
}
