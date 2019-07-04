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

import javax.inject.Inject;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.json.basic.JsonMapObject;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ilb.filedossier.api.DossierContextResource;
import ru.ilb.filedossier.api.DossierFileResource;
import ru.ilb.filedossier.api.DossierResource;
import ru.ilb.filedossier.api.DossiersResource;
import ru.ilb.filedossier.view.DossierView;

/**
 *
 * @author slavb
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DossiersResourceImplTest {

    DossiersResource resource;

    @LocalServerPort
    Integer randomPort;

    public DossiersResourceImplTest() {
    }

    private DossiersResource getDossiersResource() {
        if (resource == null) {
            String port = randomPort.toString();
            String resourceUri = "http://localhost:" + port + "/web";
            System.out.println("resourceUri=" + resourceUri);
            resource = JAXRSClientFactory.create(resourceUri, DossiersResource.class);
        }
        return resource;

    }

    /**
     * Test of getDossierResource method, of class DossiersResourceImpl.
     */
    @org.junit.Test
    public void testGetDossierResource() {
        String dossierKey = "teststorekey";
        String dossierPackage = "testmodel";
        String dossierCode = "TEST";
        DossierResource dossierResource = getDossiersResource().getDossierResource(dossierKey, dossierPackage, dossierCode);
        DossierView dossier = dossierResource.getDossier();
        assertNotNull(dossier);
        DossierFileResource dossierFileResource = dossierResource.getDossierFileResource("fairpricecalc");
        DossierContextResource dossierContextResource = dossierFileResource.getDossierContextResource();

        JsonMapObject jsonMapObject = new JsonMapObject();
        jsonMapObject.setProperty("test", "123");
        dossierContextResource.setContext(jsonMapObject);
    }

}
