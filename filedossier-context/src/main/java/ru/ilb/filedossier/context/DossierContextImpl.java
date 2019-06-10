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
package ru.ilb.filedossier.context;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import ru.ilb.filedossier.entities.DossierContext;

/**
 *
 * @author slavb
 */
public class DossierContextImpl implements Serializable, DossierContext {

    private static final long serialVersionUID = 2342354656266614361L;
    
    String contextKey;

    Map<String, Object> values = new LinkedHashMap<>();

    public DossierContextImpl(String contextKey) {
        this.contextKey = contextKey;
    }

    public DossierContextImpl(String contextKey, Map<String, Object> values) {
        this.contextKey = contextKey;
        this.values = values;
    }

    @Override
    public String getContextKey() {
        return contextKey;
    }
    
    @Override
    public Map<String, Object> asMap() {
        return values;
    }

    @Override
    public void setProperty(String name, Object value) {
        values.put(name, value);
    }

    @Override
    public boolean containsProperty(String name) {
        return values.containsKey(name);
    }

    @Override
    public Object getProperty(String name) {
        return values.get(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DossierContextImpl other = (DossierContextImpl) obj;
        if (!Objects.equals(this.values, other.values)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
