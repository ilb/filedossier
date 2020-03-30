/*
 * Copyright 2019 SPoket.
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

import java.util.List;

/**
 * Dossier file is an extension of DossierContents, it is the abstract dossier file, that contains representation, context, and abstract flags of file.
 *
 * @author SPoket
 */
public interface DossierFile extends DossierContents {

    /**
     * @return is file required to be present.
     */
    boolean getRequired();

    /**
     * @return is file readonly (can't be uploaded by user)
     */
    boolean getReadonly();

    /**
     * @return is file hidden (not shown to user)
     */
    boolean getHidden();

    /**
     * @return is file exists
     */
    boolean getExists();

    /**
     * @return true if allowed to store multiple files to dossier file
     */
    boolean getAllowedMultiple();

    /**
     * @return dossier file context key
     */
    String getContextKey();

    /**
     * Returns last modified date of latest version
     * FIXME лучше использовать LocalDateTime
     * @return millis
     */
    String lastModified();

    /**
     * @return count of file versions
     */
    Integer getVersionsCount();



    /**
     * Creates new dossier file version
     * @param mediaType
     * @return
     */
    DossierFileVersion createNewVersion(String mediaType);

    /**
     * @param version
     * @return specified dossier file version
     */
    DossierFileVersion getVersion(int version);

    /**
     * @return latest dossier file version
     */
    DossierFileVersion getLatestVersion();
}
