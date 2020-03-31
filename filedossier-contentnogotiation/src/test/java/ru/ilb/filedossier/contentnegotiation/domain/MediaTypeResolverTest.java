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
package ru.ilb.filedossier.contentnegotiation.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.core.MediaType;
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
public class MediaTypeResolverTest {

    public MediaTypeResolverTest() {
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
     * Test of getAcceptableMediaType method, of class MediaTypeResolver.
     */
    @Test
    public void testGetAcceptableMediaType() {
        System.out.println("getAcceptableMediaType");
        List<MediaType> allowedMediaTypes = Arrays.asList(MediaType.valueOf(MediaType.APPLICATION_XHTML_XML), MediaType.valueOf(MediaType.APPLICATION_XML));
        String acceptableMediaTypes = "*/*";
        MediaTypeAcceptor mta = new MediaTypeAcceptor(acceptableMediaTypes);
        MediaTypeResolver instance = new MediaTypeResolver(mta);
        Optional<MediaType> expResult = Optional.of(MediaType.valueOf(MediaType.APPLICATION_XHTML_XML));

        Optional<MediaType> result = instance.getAcceptableMediaType(allowedMediaTypes);
        assertEquals(expResult, result);

    }

}
