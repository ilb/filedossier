package ru.ilb.filedossier.components;

import java.net.URISyntaxException;
import java.util.Arrays;
import javax.inject.Inject;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.json.basic.JsonMapObject;
import org.apache.cxf.jaxrs.provider.json.JsonMapObjectProvider;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ilb.filedossier.api.DossierContextResource;
import ru.ilb.filedossier.api.DossiersResource;

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
/**
 *
 * @author kuznetsov_me
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DossierContextResourceImplTest {

    private DossiersResource resource;

    @LocalServerPort
    private Integer randomPort;

    @Inject
    private JsonMapObjectProvider jsonMapObjectProvider;

    private DossiersResource getDossiersResource() {
        if (resource == null) {
            String port = randomPort.toString();
            String resourceUri = "http://localhost:" + port + "/web";
            System.out.println("resourceUri=" + resourceUri);
            resource = JAXRSClientFactory.create(resourceUri, DossiersResource.class,
                    Arrays.asList(jsonMapObjectProvider));
        }
        return resource;

    }

    private DossierContextResource getDossierContextResource(String name) {
        return getDossiersResource().getDossierResource("teststorekey", "testmodel", "TEST", "mode1")
                .getDossierFileResource(name).getDossierContextResource();
    }

    /**
     * Test of getDossierResource method, of class DossiersResourceImpl.
     */
    @org.junit.Test
    public void testGetContext() throws URISyntaxException {
        DossierContextResource contextResource = getDossierContextResource("jurnals");
        JsonMapObject context = contextResource.getContext();
        Assert.assertNotNull(context.asMap());
    }
}
