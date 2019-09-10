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

package ru.ilb.filedossier.entities;

/**
 * Dossier file attribute.
 * @author kuznetsov_me
 */
public final class DossierFileAttribute {

    /**
     * Attribute code for querying.
     */
    private String code;

    /**
     * Abstract name for users.
     */
    private String name;

    /**
     * Base constructor for DossierFileAttribute.
     * @param code code of new attribute
     * @param name name of new attribute
     */
    public DossierFileAttribute(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
