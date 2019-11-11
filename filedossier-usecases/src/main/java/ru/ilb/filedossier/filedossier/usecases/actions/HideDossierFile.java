package ru.ilb.filedossier.filedossier.usecases.actions;

import ru.ilb.filedossier.context.DossierContextService;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.entities.DossierFile;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class HideDossierFile {

    private final DossierContextService contextService;

    @Inject
    public HideDossierFile(DossierContextService contextService) {
        this.contextService = contextService;
    }

    public void hide(DossierFile dossierFile) {
        DossierContext context = contextService.getContext(dossierFile.getContextKey());
        context.setProperty("hidden", "true");
        contextService.putContext(dossierFile.getContextKey(), context);
    }
}
