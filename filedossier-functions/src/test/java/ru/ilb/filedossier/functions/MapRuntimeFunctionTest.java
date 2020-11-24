/*
 * Copyright 2019 chunaev.
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
package ru.ilb.filedossier.functions;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.apache.cxf.jaxrs.json.basic.JsonMapObject;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author chunaev
 */
public class MapRuntimeFunctionTest {

    public MapRuntimeFunctionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of apply method, of class MapRuntimeFunction.
     */
    @Test
    public void testApply() throws URISyntaxException {
        System.out.println("apply of MapRuntimeFunction");
        Map<String, Object> t = new HashMap();
        JsonMapObject testObject = new JsonMapObject();
        testObject.setProperty("test", "123");
        t.put("test", testObject.asMap());
        URI commandUri = this.getClass().getClassLoader().getResource("runtime/command.sh").toURI();
        Paths.get(commandUri.getPath()).toFile().setExecutable(true);
        MapRuntimeFunction instance = new MapRuntimeFunction(commandUri);
        Map<String, Object> expResult = t;
        Map<String, Object> result = instance.apply(t);
        System.out.println(result);
        assertEquals(expResult, result);
    }

}
