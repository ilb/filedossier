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
package ru.ilb.filedossier.representation.delegate;

import ru.ilb.filedossier.entities.RepresentationPart;

/**
 * MultipartDelegate delegates work with multi part store of representation,
 * allows to work with representation parts (e.g. scans): save, combine, check is empty.
 *
 * @author kuznetsov_me
 */
public interface MultipartDelegate {

    /**
     * Saves representation part to store.
     *
     * @param part abstract part of representation
     */
    public void setRepresentationPart(RepresentationPart part);

    /**
     * @return combined parts within one document
     */
    public byte[] getContents();

    /**
     * Checks if parts store is empty.
     * @return boolean value
     */
    public boolean isEmpty();

}
