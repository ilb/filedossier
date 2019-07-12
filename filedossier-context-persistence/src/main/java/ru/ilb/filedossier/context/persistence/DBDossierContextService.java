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

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.context.persistence.model.DossierContextPersistence;
import ru.ilb.filedossier.context.persistence.repositories.DossierContextRepository;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.context.DossierContextService;

// TODO: create idMap

/**
 * Implementation of DossierContextService for database
 * 
 * @see ru.ilb.filedossier.context.DossierContextService
 * 
 * @author kuznetsov_me
 */
@Named
public class DBDossierContextService implements DossierContextService {

    @Inject
    private DossierContextRepository repository;

    public DBDossierContextService(DossierContextRepository repository) {
	this.repository = repository;
    }

    public DBDossierContextService() {
    }

    /**
     * Returns DossierContext with not/exist context values
     * 
     * @param contextKey
     * @return
     */
    @Override
    public DossierContext getContext(String contextKey) {
	DossierContextPersistence contextPersistence = repository.findByContextKey(contextKey)
		.orElse(new DossierContextPersistence());
	return new DossierContextImpl(contextPersistence.asMap());
    }

    /**
     * Puts context if not exist in database, and merges if exist
     * 
     * @param contextKey
     * @param dossierContext
     */
    @Override
    public void putContext(String contextKey, DossierContext dossierContext) {
	Optional<DossierContextPersistence> oldContext = repository.findByContextKey(contextKey);
	DossierContextPersistence updatedContext = new DossierContextPersistence(contextKey, dossierContext.asMap());
	oldContext.ifPresent((c) -> updatedContext.setId(oldContext.get().getId()));
	repository.save(updatedContext);
    }
}
