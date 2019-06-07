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
package ru.ilb.filedossier.context.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.context.persistence.model.DossierContextPersistence;
import ru.ilb.filedossier.context.persistence.repositories.DossierContextRepository;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.entities.DossierContextService;


public class DBDossierContextService implements DossierContextService {

    @Autowired //?
    DossierContextRepository repository;
    
    @Override
    public DossierContext getContext(String contextKey) {
        DossierContextPersistence contextPersistence = repository.findByContextKey(contextKey);
        DossierContext context = new DossierContextImpl();
        context.setProperty("contextKey", contextPersistence.getContextKey());
        
        contextPersistence.getDossierContextData().forEach((data) -> {
            context.setProperty(data.getDataKey(), data.getDataValue());
        });
        return context;
    }

    @Override
    public void putContext(DossierContext context) {
        repository.save(context);
    }
    
}
