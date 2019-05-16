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
package ru.ilb.filedossier.components;

import java.io.InputStream;
import javax.ws.rs.core.Response;
import ru.ilb.filedossier.api.DossierResource;
import ru.ilb.filedossier.view.Dossier;


public class DossierResourceImpl implements DossierResource {

    private final String dossierKey;

    private final String dossierCode;

    public DossierResourceImpl(String dossierKey, String dossierCode) {
        this.dossierKey = dossierKey;
        this.dossierCode = dossierCode;
    }


    @Override
    public Dossier getDossier() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getContents(String fileCode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putContents(String fileCode, InputStream inputstream) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
