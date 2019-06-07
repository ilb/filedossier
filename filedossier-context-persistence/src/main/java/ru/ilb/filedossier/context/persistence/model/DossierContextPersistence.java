/*
 * Copyright 2019 slavb.
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
package ru.ilb.filedossier.context.persistence.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.annotation.Id;

/**
 *
 * @author slavb
 */
public class DossierContextPersistence {

    @Id
    private Long id;
    
    private String contextKey;
    
    private Set<DossierContextData> dossierContextDatas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContextKey() {
        return contextKey;
    }

    public void setContextKey(String contextKey) {
        this.contextKey = contextKey;
    }
    
    public List<DossierContextData> getDossierContextData() {
        return (List<DossierContextData>) dossierContextDatas;
    }
    
    public void addDossierContextData(String key, String value) {
        dossierContextDatas.add(createContextData(key, value));
    }
    
    private DossierContextData createContextData(String key, String value) {
        DossierContextData contextData = new DossierContextData(key, value);
        return contextData;
    }
    
}
