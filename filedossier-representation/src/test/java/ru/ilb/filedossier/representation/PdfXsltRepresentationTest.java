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
package ru.ilb.filedossier.representation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author kuznetsov_me
 */
public class PdfXsltRepresentationTest {

    public PdfXsltRepresentationTest() {
    }

    @Test
    public void testGenerateRepresentation() throws URISyntaxException, IOException {
        System.out.println("getContents");

        URI stylesheetUri = getClass().getClassLoader().getResource("projectteam/projectteam2fo.xsl").toURI();
        URI dataUri = getClass().getClassLoader().getResource("projectteam/projectteam.xml").toURI();

        byte[] source = Files.readAllBytes(Paths.get(dataUri));
        DossierContentsHolder contents = new DossierContentsHolder(source, "application/pdf", "projectteam", "Отчет",
                "pdf");

//        Store store = StoreFactory.newInstance(Files.createTempDirectory("storeroot").toUri()).getStore("storekey");
        PdfXsltRepresentation instance = new PdfXsltRepresentation("application/pdf", stylesheetUri, dataUri);
        instance.setParent(contents);

        byte[] result = instance.getContents();
        assertNotNull(result);
        assertEquals("projectteam.pdf", instance.getFileName());
        assertEquals("application/pdf", instance.getMediaType());
    }

}
