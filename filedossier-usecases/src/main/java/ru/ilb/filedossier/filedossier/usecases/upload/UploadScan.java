/*
 * Copyright 2019 kuznetsov_me.
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
package ru.ilb.filedossier.filedossier.usecases.upload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import ru.ilb.filedossier.entities.Barcode;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.Scan;
import ru.ilb.filedossier.filedossier.document.validation.DocumentArea;
import ru.ilb.filedossier.filedossier.document.validation.DocumentValidator;
import ru.ilb.filedossier.filedossier.document.validation.ScanXMPMetaProvider;
import ru.ilb.filedossier.filedossier.document.validation.SignatureDetector;
import ru.ilb.filedossier.filedossier.document.validation.SignatureValidationStrategy;
import ru.ilb.filedossier.filedossier.document.validation.ValidationReport;
import ru.ilb.filedossier.filedossier.usecases.exceptions.BarcodeNotFoundException;
import ru.ilb.filedossier.filedossier.usecases.exceptions.SignaturesNotDetectedException;
import ru.ilb.filedossier.functions.BarcodeScannerFunction;
import ru.ilb.filedossier.functions.XmpBarcodeScannerFunction;
import ru.ilb.filedossier.utils.PdfUtils;

/**
 *
 * @author kuznetsov_me
 */
@Named
public class UploadScan {

    @Inject
    private DocumentValidator validator;

    public void upload(File file, DossierFile dossierFile) {
        byte[] rawScan;
        try {
            rawScan = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error getting scan file: " + e);
        }

        Barcode barcode = getBarcode(rawScan);

        byte[] pdfPage = PdfUtils.extractPdfPage(dossierFile.getRepresentation()
                .generateRepresentation(), barcode.getPageNumber());

        List<DocumentArea> areas;
        try {
            areas = getSignatureAreas(rawScan);
        } catch (IOException e) {
            throw new RuntimeException("Error while extracting signature areas: " + e);
        }

        if (areas != null) {
            ValidationReport report;
            try {
                report = checkSignatures(pdfPage, rawScan, areas);
            } catch (IOException e) {
                throw new RuntimeException("Bad pages files: " + e);
            }

            if (!report.isOk()) {
                throw new SignaturesNotDetectedException(report.getErrors());
            }
        }

        Scan scan = new Scan(barcode.getPageNumber(), rawScan);
        saveScan(scan, dossierFile);
    }

    private Barcode getBarcode(byte[] rawScan) {
        BarcodeScannerFunction function = new XmpBarcodeScannerFunction();
        return function.apply(rawScan).orElseThrow(BarcodeNotFoundException::new);
    }

    private List<DocumentArea> getSignatureAreas(byte[] rawScan) throws IOException {
        ScanXMPMetaProvider provider = new ScanXMPMetaProvider(rawScan);
        return provider.getSignatureCoordinates();
    }

    private ValidationReport checkSignatures(byte[] originalPage,
            byte[] scan, List<DocumentArea> areas) throws IOException {

        SignatureDetector signatureDetector = SignatureDetector.newRequestBuilder()
                                              .withPDFPage(originalPage)
                                              .withScanPage(scan)
                                              .withSignatureAreas(areas)
                                              .build();

        validator.setValidationStrategy(new SignatureValidationStrategy(signatureDetector));
        return validator.validate();
    }

    private void saveScan(Scan scan, DossierFile dossierFile) {
        dossierFile.getRepresentation().setRepresentationPart(scan);
    }
}
