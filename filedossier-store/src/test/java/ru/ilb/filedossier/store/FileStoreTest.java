/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ilb.filedossier.store;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author develop01
 */
public class FileStoreTest {

    private static final URI TEST_STORE_ROOT;
    private static final String TEST_STORE_KEY = "teststorekey";
    private static final String TEST_KEY = "testkey";
    private static final byte[] TEST_DATA = "test data".getBytes();

    static {
        try {
            TEST_STORE_ROOT = FileStoreTest.class.getClassLoader().getResource("teststoreroot").toURI();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

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
     * Test of setContents method, of class FileStore.
     */
    @Test(expected = InvalidFileNameException.class)
    public void testPutContentsThrowsException() throws IOException {
        System.out.println("putContents: throws InvalidFileNameException");
        String key = "";
        String storeKey = "";
        FileStore instance = new FileStore(TEST_STORE_ROOT, storeKey);
        instance.setContents(key, TEST_DATA);
    }

    @Test
    public void testMultipleStoreKeys() throws IOException {
        System.out.println("putContents: throws InvalidFileNameException");
        String firstKey = "storekey";
        String secondKey = "file";

        FileStore store = new FileStore(TEST_STORE_ROOT, firstKey);
        FileStore nestedStore = store.getNestedFileStore(secondKey);

        store.setContents("file1.pdf", TEST_DATA);
        nestedStore.setContents("file1.png", TEST_DATA);
        nestedStore.setContents("file2.png", TEST_DATA);

        byte[] result = store.getContents("file1.pdf");
        byte[] nestedResult = nestedStore.getContents("file1.png");
        assertEquals(new String(TEST_DATA), new String(result));
        assertEquals(new String(TEST_DATA), new String(nestedResult));
    }

    /**
     * Test of setContents method, of class FileStore.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testPutContents() throws IOException {
        System.out.println("putContents");
        FileStore instance = new FileStore(TEST_STORE_ROOT, TEST_STORE_KEY);
        instance.setContents(TEST_KEY, TEST_DATA);
    }

    /**
     * Test of getContents method, of class FileStore.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetContents() throws Exception {
        System.out.println("getContents");
        byte[] expectedResult = IOUtils.toByteArray(this.getClass().getResourceAsStream("fairpricecalc.test1.xml"));
        FileStore instance = new FileStore(TEST_STORE_ROOT, TEST_STORE_KEY);
        instance.setContents("fairpricecalc.xml", expectedResult);
        byte[] result = instance.getContents("fairpricecalc.xml");
        assertArrayEquals(expectedResult, result);
    }

}
