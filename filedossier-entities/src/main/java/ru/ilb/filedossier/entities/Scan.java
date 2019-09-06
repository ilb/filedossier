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
 * Image scan implementation of RepresentationPart.
 * @see ru.ilb.filedossier.entities.RepresentationPart
 * @author kuznetsov_me
 */
public class Scan implements RepresentationPart {

    private int pageNumber;

    private byte[] contents;

    public Scan(int pageNumber, byte[] contents) {
        this.pageNumber = pageNumber;
        this.contents = contents;
    }

    /**
     * @return scan page number as code
     */
    @Override
    public String getCode() {
        return String.valueOf(pageNumber);
    }

    /**
     * @return scan page in byte array
     */
    @Override
    public byte[] getContents() {
        return contents;
    }
}
