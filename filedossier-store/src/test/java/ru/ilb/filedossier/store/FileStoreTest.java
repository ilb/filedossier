/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ilb.filedossier.store;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
     * Test of putContents method, of class FileStore.
     */
    @Test(expected = InvalidFileNameException.class)
    public void testPutContentsThrowsException() throws IOException {
        System.out.println("putContents: throws InvalidFileNameException");
        String key = "";
        String storeKey = "";
        FileStore instance = new FileStore(storeKey, TEST_STORE_ROOT);
        instance.putContents(key, TEST_DATA);
    }
    
    /**
     * Test of putContents method, of class FileStore.
     * @throws java.io.IOException
     */
    @Test
    public void testPutContents() throws IOException {
        System.out.println("putContents");
        FileStore instance = new FileStore(TEST_STORE_KEY, TEST_STORE_ROOT);
        instance.putContents(TEST_KEY, TEST_DATA);
    }

    /**
     * Test of getContents method, of class FileStore.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetContents() throws Exception {
        System.out.println("getContents");
        byte[] expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><active>True</active><fairPrice>956.05</fairPrice><countDays>20</countDays><countDeals>1543</countDeals><tradingVolume>1.72</tradingVolume><initialVolume>350000000</initialVolume><date>2019-05-21</date><marketDatas><marketData><countDeals>89</countDeals><tradingVolume>4623288.52</tradingVolume><weightedAverage>94.994</weightedAverage></marketData><marketData><countDeals>78</countDeals><tradingVolume>301932884.18</tradingVolume><weightedAverage>94.851</weightedAverage></marketData><marketData><countDeals>126</countDeals><tradingVolume>1186610720.6</tradingVolume><weightedAverage>94.81</weightedAverage></marketData><marketData><countDeals>125</countDeals><tradingVolume>671092941.71</tradingVolume><weightedAverage>94.597</weightedAverage></marketData><marketData><countDeals>61</countDeals><tradingVolume>275768713.58</tradingVolume><weightedAverage>94.724</weightedAverage></marketData><marketData><countDeals>77</countDeals><tradingVolume>217470742.85</tradingVolume><weightedAverage>94.894</weightedAverage></marketData><marketData><countDeals>61</countDeals><tradingVolume>2032234.62</tradingVolume><weightedAverage>95.053</weightedAverage></marketData><marketData><countDeals>13</countDeals><tradingVolume>58038.11</tradingVolume><weightedAverage>95.144</weightedAverage></marketData><marketData><countDeals>33</countDeals><tradingVolume>37193528.4</tradingVolume><weightedAverage>95.09</weightedAverage></marketData><marketData><countDeals>81</countDeals><tradingVolume>2083994.38</tradingVolume><weightedAverage>95.029</weightedAverage></marketData><marketData><countDeals>37</countDeals><tradingVolume>1085429.45</tradingVolume><weightedAverage>95.046</weightedAverage></marketData><marketData><countDeals>78</countDeals><tradingVolume>100911889.1</tradingVolume><weightedAverage>94.958</weightedAverage></marketData><marketData><countDeals>20</countDeals><tradingVolume>5565761.23</tradingVolume><weightedAverage>95.011</weightedAverage></marketData><marketData><countDeals>79</countDeals><tradingVolume>98318461.84</tradingVolume><weightedAverage>94.998</weightedAverage></marketData><marketData><countDeals>69</countDeals><tradingVolume>10185810.07</tradingVolume><weightedAverage>94.999</weightedAverage></marketData><marketData><countDeals>229</countDeals><tradingVolume>2115946744.23</tradingVolume><weightedAverage>95.206</weightedAverage></marketData><marketData><countDeals>105</countDeals><tradingVolume>218171053.35</tradingVolume><weightedAverage>95.374</weightedAverage></marketData><marketData><countDeals>68</countDeals><tradingVolume>49358650.26</tradingVolume><weightedAverage>95.52</weightedAverage></marketData><marketData><countDeals>57</countDeals><tradingVolume>193513418.25</tradingVolume><weightedAverage>95.591</weightedAverage></marketData><marketData><countDeals>57</countDeals><tradingVolume>235021208.65</tradingVolume><weightedAverage>95.605</weightedAverage></marketData></marketDatas></root>".getBytes();
        FileStore instance = new FileStore(TEST_STORE_KEY, TEST_STORE_ROOT);
        byte[] result = instance.getContents("fairpricecalc.xml");
        assertArrayEquals(expectedResult, result);
    }
    
}
