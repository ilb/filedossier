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

import java.util.Optional;
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
public class MediaTypeAcceptorTest {

    public MediaTypeAcceptorTest() {
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
     * Test of getCompatibleMediaType method, of class MediaTypeAcceptor.
     */
    @Test
    public void testGetCompatibleMediaType() {
        System.out.println("getCompatibleMediaType");
        MediaType other = MediaType.valueOf("text/html");
        String acceptableMediaTypes = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        MediaTypeAcceptor instance = new MediaTypeAcceptor(acceptableMediaTypes);
        Optional<MediaType> expResult = Optional.ofNullable(MediaType.valueOf("text/html"));
        Optional<MediaType> result = instance.getCompatibleMediaType(other);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCompatibleMediaType2() {
        System.out.println("getCompatibleMediaType2");
        MediaType other = MediaType.valueOf("application/xml");
        String acceptableMediaTypes = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        MediaTypeAcceptor instance = new MediaTypeAcceptor(acceptableMediaTypes);
        Optional<MediaType> expResult = Optional.ofNullable(MediaType.valueOf("application/xml;q=0.9"));
        Optional<MediaType> result = instance.getCompatibleMediaType(other);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCompatibleMediaType3() {
        System.out.println("getCompatibleMediaType3");
        MediaType other = MediaType.valueOf("application/pdf");
        String acceptableMediaTypes = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        MediaTypeAcceptor instance = new MediaTypeAcceptor(acceptableMediaTypes);
        Optional<MediaType> expResult = Optional.ofNullable(MediaType.valueOf("*/*;q=0.8"));
        Optional<MediaType> result = instance.getCompatibleMediaType(other);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCompatibleMediaType4() {
        System.out.println("getCompatibleMediaType3");
        MediaType other = MediaType.valueOf("application/pdf");
        String acceptableMediaTypes = "text/html,application/xhtml+xml";
        MediaTypeAcceptor instance = new MediaTypeAcceptor(acceptableMediaTypes);
        Optional<MediaType> expResult = Optional.ofNullable(null);
        Optional<MediaType> result = instance.getCompatibleMediaType(other);
        assertEquals(expResult, result);
    }

    /**
     * Test of isAcceptDefaultRepresentation method, of class MediaTypeAcceptor.
     */
    @Test
    public void testIsAcceptDefaultRepresentation() {
        System.out.println("isAcceptDefaultRepresentation");
        String acceptableMediaTypes = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        MediaTypeAcceptor instance = new MediaTypeAcceptor(acceptableMediaTypes);
        boolean expResult = true;
        boolean result = instance.isAcceptDefaultRepresentation();
        assertEquals(expResult, result);
    }

    @Test
    public void testIsAcceptDefaultRepresentation2() {
        System.out.println("isAcceptDefaultRepresentation2");
        String acceptableMediaTypes = "application/xhtml+xml,text/html,application/xml;q=0.9,*/*;q=0.8";
        MediaTypeAcceptor instance = new MediaTypeAcceptor(acceptableMediaTypes);
        boolean expResult = true;
        boolean result = instance.isAcceptDefaultRepresentation();
        assertEquals(expResult, result);
    }

  @Test
    public void testIsAcceptDefaultRepresentation3() {
        System.out.println("isAcceptDefaultRepresentation3");
        String acceptableMediaTypes = "application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        MediaTypeAcceptor instance = new MediaTypeAcceptor(acceptableMediaTypes);
        boolean expResult = false;
        boolean result = instance.isAcceptDefaultRepresentation();
        assertEquals(expResult, result);
    }
}
