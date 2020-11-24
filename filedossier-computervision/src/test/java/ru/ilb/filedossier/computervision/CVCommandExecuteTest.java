/*
 * Copyright 2019 kudrin.
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
package ru.ilb.filedossier.computervision;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;
import org.json.XML;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ru.ilb.filedossier.computervision.barcodedetectrequest.AreaType;
import ru.ilb.filedossier.computervision.barcodedetectrequest.BarcodeDetectRequest;
import ru.ilb.filedossier.computervision.barcodedetectrequest.BarcodeType;
import ru.ilb.filedossier.computervision.barcodedetectrequest.PagesType;
import ru.ilb.filedossier.computervision.barcodedetectrequest.SourceType;
import ru.ilb.filedossier.computervision.process.ComputerVistionProcesss;
import ru.ilb.filedossier.computervision.signaturedetectrequest.Signature;
import ru.ilb.filedossier.computervision.signaturedetectrequest.SignatureDetectRequest;

/**
 *
 * @author kudrin
 */
@Ignore
public class CVCommandExecuteTest {

    public CVCommandExecuteTest() {
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

    @Test
    public void jsonTest() {
        SignatureDetectRequest req = createSignatureDetectRequest();
        String json = toJsonString(req);
        System.out.println(json);
    }

    // get file from classpath, resources folder
//    public File getFileFromResources(String fileName) {
//        ClassLoader classLoader = getClass().getClassLoader();
//        URL resource = classLoader.getResource(fileName);
//
//        if (resource == null) {
//            throw new IllegalArgumentException("file is not found!");
//        } else {
//            return new File(resource.getFile());
//        }
//    }

    private File createTempJsonFile(String fileName, String jsonText) throws IOException {
        File file = File.createTempFile(fileName, ".json");

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(jsonText);
        bw.close();

        return file;
    }

    @Test
    public void executeSignaturesTest() throws IOException, InterruptedException {

        File inputFile = createTempJsonFile("documentsignaturedetector", toJsonString(createSignatureDetectRequest()));

        ComputerVistionProcesss p = ComputerVistionProcessFactory.createProcess(ComputerVistionProcessType.SIGNATURE_DETECTION, inputFile);
        String output = p.execute();
        inputFile.delete();

        System.out.println(output);
        System.out.println(toXmlString(output));
    }

    @Test
    public void executeBarcodesTest() throws IOException, InterruptedException {
        File inputFile = createTempJsonFile("documentbarcodedetector", toJsonString(createBarcodeDetectRequest()));

        ComputerVistionProcesss p = ComputerVistionProcessFactory.createProcess(ComputerVistionProcessType.BARCODE_DETECTION, inputFile);
        String output = p.execute();
        inputFile.delete();

        System.out.println(output);
        System.out.println(toXmlString(output));
    }

    private static String toXmlString(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        String xml = XML.toString(json, "root");
        return xml;
    }

    private static String toJsonString(Object request) {
        JSONObject jsonObject = new JSONObject(request);
        return jsonObject.toString();
    }

    private SignatureDetectRequest createSignatureDetectRequest() {
        SignatureDetectRequest request = new SignatureDetectRequest();
        request.setPdfPage("D:/citycard/projects/ComputerVision/DocumentSignatureDetector/examples/images/1/page_20.png");
        request.setScanPage("D:/citycard/projects/ComputerVision/DocumentSignatureDetector/examples/images/2/page_7.png");

        request.getSignatures().add(new Signature().withLeft(0).withTop(0).withRight(10).withBottom(10));
        request.getSignatures().add(new Signature().withLeft(16).withTop(114).withRight(101).withBottom(129));
        request.getSignatures().add(new Signature().withLeft(110).withTop(114).withRight(195).withBottom(129));
        request.getSignatures().add(new Signature().withLeft(16).withTop(267).withRight(101).withBottom(284));
        request.getSignatures().add(new Signature().withLeft(110).withTop(267).withRight(195).withBottom(284));

        return request;
    }

    private BarcodeDetectRequest createBarcodeDetectRequest() {
        BarcodeDetectRequest request = new BarcodeDetectRequest();

        request.withPages(new PagesType().withDirectory("D:/citycard/projects/ComputerVision/documentbarcode/examples/images").withPrefix("page_"));

        BarcodeType barcode = new BarcodeType().withArea(new AreaType()
                .withLeft(34)
                .withTop(95)
                .withRight(51)
                .withLower(97.5f));

        request.withSource(new SourceType()
                .withDocument("D:/citycard/projects/ComputerVision/documentbarcode/examples/pdf/avto.pdf")
                .withBarcode(barcode));

        return request;
    }
}
