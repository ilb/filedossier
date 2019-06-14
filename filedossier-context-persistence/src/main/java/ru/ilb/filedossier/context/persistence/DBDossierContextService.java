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

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.context.persistence.model.DossierContextPersistence;
import ru.ilb.filedossier.context.persistence.repositories.DossierContextRepository;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.entities.DossierContextService;

public class DBDossierContextService implements DossierContextService {

    private DossierContextRepository repository;

    public DBDossierContextService(DossierContextRepository repository) {
	this.repository = repository;
    }

    @Override
    public DossierContext getContext(String contextKey) {
	DossierContextPersistence contextPersistence = repository.findByContextKey(contextKey);
	DossierContextImpl context = new DossierContextImpl(contextPersistence.getContextKey(),
		contextPersistence.asMap());
	return context;
    }

    @Override
    public void putContext(DossierContext context) throws DbActionExecutionException {
	DossierContextPersistence contextPersistence = new DossierContextPersistence(context.getContextKey(),
		context.asMap());
	repository.save(contextPersistence);
    }

    @Override
    public void mergeContext(DossierContext context) {
	DossierContextPersistence oldContext = repository.findByContextKey(context.getContextKey());
	DossierContextPersistence newContext = new DossierContextPersistence(context.getContextKey(), context.asMap());
	newContext.setId(oldContext.getId());
	repository.save(newContext);
    }

}
