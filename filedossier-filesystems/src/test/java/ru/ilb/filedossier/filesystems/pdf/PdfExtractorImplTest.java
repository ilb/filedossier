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
package ru.ilb.filedossier.filesystems.pdf;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class PdfExtractorImplTest {

    public PdfExtractorImplTest() {
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
     * Test of extract method, of class PdfToJpegFilesImpl.
     */
    @Test
    public void testExtract() throws IOException, URISyntaxException {
        System.out.println("extract");
        URI pdfUri = this.getClass().getResource("test.pdf").toURI();

        Path folder = Files.createTempDirectory("PdfToJpegFilesImplTest");
        folder.toFile().deleteOnExit();
        PdfExtractorImpl instance = new PdfExtractorImpl();
        instance.extract(pdfUri, folder, "page");
        String pageFileName = "page-000.jpg";
        Path pagePath = folder.resolve(pageFileName);
        File pageFile = pagePath.toFile();
        assertTrue(pageFileName + " exists", pageFile.exists());
//        URI expectedPageUri = this.getClass().getResource("page-000.jpg").toURI();
//        File expectedPageFile = Paths.get(expectedPageUri).toFile();
//        
        //assertBinaryEquals
    }

}
