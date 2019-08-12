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
package ru.ilb.filedossier.entities;

import java.util.Map;

/**
 * <p>
 * Provides interface for work with context (String, Object) for a certain entity. Context needed to
 * save properties.
 * <p>
 *
 * @author slavb
 */
public interface DossierContext {

    /**
     * @return context values as map
     */
    public Map<String, Object> asMap();

    public boolean containsProperty(String propertyName);

    public Object getProperty(String propertyName);

    public void setProperty(String propertyName, Object propertyValue);
}
