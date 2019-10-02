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
package ru.ilb.filedossier.components;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.provider.json.JsonMapObjectProvider;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ilb.filedossier.api.DossierFileResource;
import ru.ilb.filedossier.api.DossiersResource;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 *
 * @author kuznetsov_me
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DossierFileResourceImplTest {

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

    private DossierFileResource getDossierFileResource(String name) {
        return getDossiersResource().getDossierResource("teststorekey", "testmodel", "TEST")
                .getDossierFileResource(name);
    }

    /**
     * Test of getDossierResource method, of class DossiersResourceImpl.
     */
    @org.junit.Test
    public void testAUploadContents() throws URISyntaxException {

        DossierFileResource fileResource = getDossierFileResource("jurnals");
        fileResource.upload(Paths.get(getClass()
                .getClassLoader()
                .getResource("page1.jpg")
                .toURI())
                .toFile());
    }

    @org.junit.Test
    public void testBGetContents() {

        DossierFileResource fileResource = getDossierFileResource("fairpricecalc");
        Response response = fileResource.download(null);
        Assert.assertEquals("application/vnd.oasis.opendocument.spreadsheet",
                response.getMediaType().toString());

        fileResource = getDossierFileResource("jurnals");
        response = fileResource.download(null);
        Assert.assertEquals("application/pdf", response.getMediaType().toString());
    }
}
