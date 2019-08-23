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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 *
 * @author slavb
 */
@Table(value = "DOSSIERCONTEXT")
public class DossierContextPersistence {

    @Id
    Long id;

    private String contextKey;

    private Set<DossierContextData> dossierContextDatas = new HashSet<>();

    public DossierContextPersistence() {
    }

    public DossierContextPersistence(String contextKey, Map<String, Object> properties) {
        this.contextKey = contextKey;
        properties.forEach((key, value) -> {
            dossierContextDatas.add(new DossierContextData(key, value));
        });
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContextKey() {
        return contextKey;
    }

    public Map<String, Object> asMap() {
        return dossierContextDatas.stream()
                .collect(Collectors.toMap(data -> data.getDataKey(), data -> data.getDataValue()));
    }

    private DossierContextData createContextData(String key, Object value) {
        DossierContextData contextData = new DossierContextData(key, value);
        return contextData;
    }

}
