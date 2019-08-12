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

/**
 * Dossier file is an extension of DossierContents, it is the abstract dossier file, that contains
 * representation, context, and abstract flags of file.
 *
 * @author SPoket
 */
public interface DossierFile extends DossierContents {

    /**
     * Store given file as contents of this dossier file.
     */
    public void setContents(File file);

    /**
     * @return is file required to be present.
     */
    public boolean getRequired();

    /**
     * @return is file readonly (can't be uploaded by user)
     */
    public boolean getReadonly();

    /**
     * @return is file hidden (not shown to user)
     */
    public boolean getHidden();

    /**
     * @return is file exists
     */
    public boolean getExists();

    /**
     * @return default representation of this dossier file
     * @see ru.ilb.filedossier.entities.Representation
     */
    public Representation getRepresentation();

    /**
     * @return context of this dossier file
     * @see ru.ilb.filedossier.entities.DossierContext
     */
    public DossierContext getContext();

    /**
     * Sets context for dossier file.
     *
     * @see ru.ilb.filedossier.entities.DossierContext
     */
    public void setDossierContext(DossierContext context);
}
