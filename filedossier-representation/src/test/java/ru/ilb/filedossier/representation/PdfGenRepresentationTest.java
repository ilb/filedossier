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
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author kuznetsov_me
 */
public class PdfGenRepresentationTest {

    String xsl = "https://devel.net.ilb.ru/meta/stylesheets/doctemplates/primary/loantreaty/credits/auto/V20140707.xsl";

    String xsd = "https://devel.net.ilb.ru/meta/schemas/doctemplates/primary/loantreaty/credits/auto/V20140707.xsd";

    String meta = "https://devel.net.ilb.ru/meta/&uid=doctree:11f462ebdb14a5673ff41a5c75c5176552fad343:";

    byte[] representation;

    public PdfGenRepresentationTest() {

    }

    /**
     * Test of getContents method, of class XmlPdfRepresentation.
     */
    @Test
    public void testGetContents() throws URISyntaxException, IOException {
        System.out.println("getContents");
        PdfGenRepresentation instance = new PdfGenRepresentation("application/pdf", URI.create(xsl),
                URI.create(xsd), URI.create(meta));

        URI dataUri = getClass().getClassLoader().getResource("jurnals/avto.xml").toURI();
        byte[] source = Files.readAllBytes(Paths.get(dataUri));
        DossierContentsHolder contents = new DossierContentsHolder(source, "application/pdf",
                "jurnals", "Jurnals", "pdf");
        instance.setParent(contents);
        representation = instance.getContents();
        assertNotNull(representation);
        Files.write(Paths.get(System.getProperty("java.io.tmpdir") + "/result"), representation);
    }
}
