/*
 * Copyright 2020 slavb.
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
package ru.ilb.filedossier.contentnegotiation.sevice;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author slavb
 */
public class ContentNegotiationSeviceImplTest {

    public ContentNegotiationSeviceImplTest() {
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
     * Test of getAcceptableMediaType method, of class ContentNegotiationSeviceImpl.
     */
    @Test
    public void testGetAcceptableMediaType() {
        System.out.println("getAcceptableMediaType");
        String acceptableMediaTypes = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";

        List<String> allowedMediaTypes = Arrays.asList("application/pdf", "application/xml");
        ContentNegotiationSeviceImpl instance = new ContentNegotiationSeviceImpl();
        Optional<String> expResult = Optional.of("application/pdf");
        Optional<String> result = instance.getAcceptableMediaType(acceptableMediaTypes, allowedMediaTypes);
        assertEquals(expResult, result);

    }

}
