/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ilb.filedossier.store;

import java.io.IOException;
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
public class FileStoreTest {
    
    private static final String TEST_STORE_ROOT = "teststoreroot";
    private static final String TEST_STORE_KEY = "teststorekey";
    private static final String TEST_KEY = "testkey";
    private static final byte[] TEST_DATA = "test data".getBytes();
    
    public FileStoreTest() {
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
     * Test of putContents method, of class FileStore.
     */
    @Test(expected = InvalidFileNameException.class)
    public void testPutContentsThrowsException() throws IOException {
        System.out.println("putContents: throws InvalidFileNameException");
        String key = "";
        String storeKey = "";
        String storeRoot = "";
        FileStore instance = new FileStore(storeKey, storeRoot);
        instance.putContents(key, TEST_DATA);
    }
    
    /**
     * Test of putContents method, of class FileStore.
     */
    @Test
    public void testPutContents() throws IOException {
        System.out.println("putContents");
        FileStore instance = new FileStore(TEST_STORE_KEY, TEST_STORE_ROOT);
        instance.putContents(TEST_KEY, TEST_DATA);
    }

    /**
     * Test of getContents method, of class FileStore.
     */
    @Test
    public void testGetContents() throws Exception {
        System.out.println("getContents");
        byte[] expectedResult = TEST_DATA;
        FileStore instance = new FileStore(TEST_STORE_KEY, TEST_STORE_ROOT);
        byte[] result = instance.getContents(TEST_KEY);
        assertArrayEquals(expectedResult, result);
    }
    
}
