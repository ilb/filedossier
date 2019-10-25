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
package ru.ilb.filedossier.representation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.Assert.*;
import org.junit.Test;
import ru.ilb.filedossier.entities.Store;
import ru.ilb.filedossier.store.StoreFactory;

/**
 *
 * @author slavb
 */
public class JsonXmlRepresentationTest {

    public JsonXmlRepresentationTest() {
    }

    /**
     * Test of getContents method, of class JsonXmlRepresentation.
     */
    @Test
    public void testGenerateRepresentation() throws URISyntaxException, IOException {
        System.out.println("getContents");
        URI dataUri = getClass().getClassLoader().getResource("fairpriceorder/data.json").toURI();
        URI dataXmlUri = getClass().getClassLoader().getResource("fairpriceorder/data.xml").toURI();

        byte[] source = Files.readAllBytes(Paths.get(dataUri));
        DossierContentsHolder contents = new DossierContentsHolder(source, "application/json", "fairpriceorder", "Отчет", "json");

        Store store = StoreFactory.newInstance(Files.createTempDirectory("storeroot").toUri()).getStore("storekey");

        JsonXmlRepresentation instance = new JsonXmlRepresentation();
        instance.setParent(contents);

        String expResult = new String(Files.readAllBytes(Paths.get(dataXmlUri)));
        String result = new String(instance.getContents());
        assertTrue(expResult.contains(result)); // assertion failed using test xml with end newline
    }

}
