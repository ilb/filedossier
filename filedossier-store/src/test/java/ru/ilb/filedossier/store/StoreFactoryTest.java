/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ilb.filedossier.store;

import ru.ilb.filedossier.entities.Store;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author develop01
 */
public class StoreFactoryTest {

    private static final URI TEST_STORE_ROOT;

    static {
	try {
	    TEST_STORE_ROOT = FileStoreTest.class.getClassLoader().getResource("teststoreroot").toURI();
	} catch (URISyntaxException ex) {
	    throw new RuntimeException(ex);
	}
    }

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
	StoreFactory result = StoreFactory.newInstance(TEST_STORE_ROOT);
	assertTrue(result instanceof StoreFactory);
    }

    /**
     * Test of getFileStorage method, of class StoreFactory.
     */
    @org.junit.Test
    public void testGetFileStorage() {
	System.out.println("getFileStorage");
	String storeKey = "testkey";
	StoreFactory instance = StoreFactory.newInstance(TEST_STORE_ROOT);
	Store result = instance.getStore(storeKey);
	assertTrue(result instanceof FileStore);
    }

}
