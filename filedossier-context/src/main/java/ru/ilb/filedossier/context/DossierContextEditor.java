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
package ru.ilb.filedossier.context;

import java.util.Optional;
import ru.ilb.filedossier.entities.DossierContext;

/**
 * This class connects context service (store, database) and context, allows modifying, getting context properties, saving context to store
 *
 * @author kuznetsov_me
 */
public class DossierContextEditor {

    private DossierContextService contextService;

    private DossierContext context;

    private String contextKey;

    public DossierContextEditor(DossierContextService service) {
        this.contextService = service;
    }

    /**
     * Adds property to committed context
     *
     * @param propertyKey
     * @param propertyValue
     * @param contextKey
     */
    public void putProperty(String propertyKey, Object propertyValue, String contextKey) {
        this.contextKey = contextKey;
        this.context = contextService.getContext(contextKey);
        context.setProperty(propertyKey, propertyValue);
    }

    /**
     * Returns Optional value for requested property key
     *
     * @param propertyKey
     * @param contextKey
     * @return
     */
    public Optional<Object> getProperty(String propertyKey, String contextKey) {
        this.contextKey = contextKey;
        this.context = contextService.getContext(contextKey);
        return Optional.ofNullable(context.asMap().get(propertyKey));
    }

    /**
     * Save modified context to context store
     */
    public void commit() {
        contextService.putContext(contextKey, context);
        this.context = null;
        this.contextKey = null;
    }

}
