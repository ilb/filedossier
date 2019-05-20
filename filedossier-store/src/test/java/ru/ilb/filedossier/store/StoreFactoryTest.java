/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ilb.filedossier.store;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author develop01
 */
public class StoreFactoryTest {
    
    public StoreFactoryTest() {
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
     * Test of newInstance method, of class StoreFactory.
     */
    @org.junit.Test
    public void testNewInstance() {
        System.out.println("newInstance");
        String storageRoot = "";
        StoreFactory result = StoreFactory.newInstance(storageRoot);
        assertTrue(result instanceof StoreFactory);
    }

    /**
     * Test of getFileStorage method, of class StoreFactory.
     */
    @org.junit.Test
    public void testGetFileStorage() {
        System.out.println("getFileStorage");
        String storeKey = "";
        String storageRoot = "";
        StoreFactory instance = StoreFactory.newInstance(storageRoot);
        Store result = instance.getFileStorage(storeKey);
        assertTrue(result instanceof FileStore);
    }
    
}
