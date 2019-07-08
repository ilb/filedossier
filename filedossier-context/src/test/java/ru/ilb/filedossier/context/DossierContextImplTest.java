/*
 * Copyright 2019 develop01.
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
package ru.ilb.filedossier.context;

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.ilb.filedossier.entities.DossierContext;

/**
 *
 * @author develop01
 */
public class DossierContextImplTest {
    
    public DossierContextImplTest() {
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
     * Test of asMap method, of class DossierContext.
     */
    @Test
    public void testAsMap() {
        System.out.println("asMap");
        DossierContext instance = new DossierContextImpl();
        instance.setProperty("testkey", "testvalue");
        Map<String, Object> expResult = null;
        Map<String, Object> result = instance.asMap();
        assertEquals("testvalue", result.get("testkey"));
    }

    /**
     * Test of setProperty method, of class DossierContext.
     */
    @Test
    public void testSetProperty() {
        System.out.println("setProperty");
        String name = "testkey";
        Object value = "testvalue";
        DossierContext instance = new DossierContextImpl();
        instance.setProperty(name, value);
        assertEquals(value, instance.getProperty(name));
    }

    /**
     * Test of containsProperty method, of class DossierContext.
     */
    @Test
    public void testContainsProperty() {
        System.out.println("containsProperty");
        String name = "notexist";
        DossierContext instance = new DossierContextImpl();
        boolean expResult = false;
        boolean result = instance.containsProperty(name);
        assertEquals(expResult, result);
        boolean expResult2 = true;
        instance.setProperty("notexist", "keyvalue");
        boolean result2 = instance.containsProperty(name);
        assertEquals(expResult2, result2);
        
    }

    /**
     * Test of getProperty method, of class DossierContext.
     */
    @Test
    public void testGetProperty() {
        System.out.println("getProperty");
        String name = "";
        DossierContext instance = new DossierContextImpl();
        Object expResult = null;
        Object result = instance.getProperty(name);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of equals method, of class DossierContext.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        DossierContext instance = new DossierContextImpl();
//        boolean expResult = false;
//        boolean result = instance.equals(obj);
//        assertEquals(expResult, result);
        DossierContext instance2 = new DossierContextImpl();
        instance2.setProperty("key", obj);
        instance.setProperty("key", obj);
        boolean expResult2 = true;
        boolean result2 = instance.equals(instance2);
        assertEquals(expResult2, result2);
        
    }

    /**
     * Test of hashCode method, of class DossierContext.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        DossierContext instance = new DossierContextImpl();
        instance.setProperty("key", "testvalue");
        int expResult = -1159346624;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
    
}
