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
public class MediaTypeQComparatorTest {

    public MediaTypeQComparatorTest() {
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
     * Test of compare method, of class MediaTypeQComparator.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        MediaType arg0 = MediaType.valueOf("application/xhtml+xml");
        MediaType arg1 = MediaType.valueOf("application/xml;q=0.9");

        MediaTypeQComparator instance = new MediaTypeQComparator();
        int expResult = 1;
        int result = instance.compare(arg0, arg1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCompare2() {
        System.out.println("compare2");
        MediaType arg0 = MediaType.valueOf("application/xhtml+xml");
        MediaType arg1 = MediaType.valueOf("application/xml");

        MediaTypeQComparator instance = new MediaTypeQComparator();
        int expResult = 0;
        int result = instance.compare(arg0, arg1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCompare3() {
        System.out.println("compare3");
        MediaType arg0 = MediaType.valueOf("application/xhtml+xml");
        MediaType arg1 = MediaType.valueOf("application/xml;q=1");

        MediaTypeQComparator instance = new MediaTypeQComparator();
        int expResult = 0;
        int result = instance.compare(arg0, arg1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCompare4() {
        System.out.println("compare4");
        MediaType arg0 = MediaType.valueOf("application/xhtml+xml;q=0.9");
        MediaType arg1 = MediaType.valueOf("application/xml");

        MediaTypeQComparator instance = new MediaTypeQComparator();
        int expResult = -1;
        int result = instance.compare(arg0, arg1);
        assertEquals(expResult, result);
    }
}
