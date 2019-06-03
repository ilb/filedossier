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

/**
 * Dossier file contents
 * @author slavb
 */
public interface DossierContents {

    /**
     * get contents using default representation
     * @return
     */
    byte[] getContents();

    /**
     * store contents to file
     *
     * @param contents
     */
    void setContents(byte[] contents);

    /**
     * get file name
     * @return
     */
    String getFileName();

    /**
     * file media type (based on default representation)
     * @return
     */
    String getMediaType();


}