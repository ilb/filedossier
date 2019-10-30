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

import java.io.File;
import java.util.List;

/**
 * Dossier file is an extension of DossierContents, it is the abstract dossier file, that contains representation, context, and abstract flags of file.
 *
 * @author SPoket
 */
public interface DossierFile extends DossierPath {

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
     * Returns last modified date of latest version
     * @return millis
     */
    String lastModified();

    /**
     * @return count of file versions
     */
    Integer getVersionsCount();

    /**
     * @return list with media types, allowed to store in current dossier file.
     */
    List<String> getAllowedMediaTypes();

    /**
     * Creates new dossier file version
     */
    DossierFileVersion createNewVersion(String mediaType);

    /**
     * @return specified dossier file version
     */
    DossierFileVersion getVersion(int version);

    /**
     * @return latest dossier file version
     */
    DossierFileVersion getLatestVersion();
}
