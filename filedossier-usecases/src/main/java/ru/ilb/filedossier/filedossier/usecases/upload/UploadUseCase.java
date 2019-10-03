package ru.ilb.filedossier.filedossier.usecases.upload;

import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.context.DossierContextService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class UploadUseCase {

    protected DossierContextService contextService;

    protected UploadUseCase(DossierContextService contextService) {
        this.contextService = contextService;
    }

    protected void setUploadTime(String contextKey) {
        final Map<String, Object> context = new HashMap<>();
        context.put("lastModified", LocalDateTime.now(ZoneId.systemDefault()));
        contextService.putContext(contextKey, new DossierContextImpl(context));
    }
}
