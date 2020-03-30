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

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * This interface is a DossierPath extension, represents the directory in the dossier path.
 * <p>
 *
 * @author slavb
 */
public interface DossierContents extends DossierPath {

    /**
     * @return file contents using default representation.
     */
    byte[] getContents() throws IOException;

    /**
     * Stores given contents to a default dir from given byte array
     *
     * @param contents data, which needs to be saved to a dossier file
     */
    void setContents(byte[] contents) throws IOException;

    /**
     * @return items's media type using default representation.
     */
    String getMediaType();

    /**
     * @return list with media types, allowed to store in current item. first one is default
     */
    public List<String> getAllowedMediaTypes();
    
    /**
     * @return file extension using default representation
     */
    public String getExtension();

    /**
     * @return file name that contains name and extension
     */
    default public String getFileName() {
        String extension = getExtension();
        return extension == null ? getCode() : getCode() + "." + getExtension();
    }
}
