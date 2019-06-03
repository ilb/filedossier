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

/**
 *
 * @author SPoket
 */
public interface DossierFile extends DossierContents {

    public String getCode();

    public String getName();

    /**
     * file is required to be present
     * @return
     */
    public boolean getRequired();

    /**
     * file is readonly (cannot be uploaded by user)
     * @return
     */
    public boolean getReadonly();

    /**
     * file id hidden (not shown to user)
     * @return
     */
    public boolean getHidden();

    /**
     * file shuld be served as attachment (Content-Disposition: attachment)
     * @return
     */
    public boolean getAttachment();

    /**
     * file is exsits
     * @return
     */
    public boolean getExists();


    /**
     * file extension
     * @return 
     */
    public String getExtension();

}
