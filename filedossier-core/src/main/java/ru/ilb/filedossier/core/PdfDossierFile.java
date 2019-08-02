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
package ru.ilb.filedossier.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import ru.ilb.filedossier.context.DossierContextEditor;
import ru.ilb.filedossier.context.DossierContextService;
import ru.ilb.filedossier.entities.Barcode;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.entities.Store;
import ru.ilb.filedossier.filedossier.document.validation.XMPMetadataBarcodeProvider;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;
import ru.ilb.filedossier.representation.PdfMultipageRepresentation;
import ru.ilb.filedossier.utils.PdfUtils;

/**
 *
 * @author kuznetsov_me
 */
public class PdfDossierFile extends DossierFileImpl {

    public PdfDossierFile(Store store, String code, String name, boolean required, boolean readonly,
                          boolean hidden, String mediaType, List<Representation> representations,
                          DossierContextService dossierContextService) {

        super(store, code, name, required, readonly, hidden, mediaType, representations,
              dossierContextService);

        this.representation.setParent(this);
    }

    @Override
    public void setContents(File contentsFile) {

        try {
            byte[] data = Files.readAllBytes(contentsFile.toPath());

            if (checkFileIsImage(contentsFile)) {
                setMultipageContents(data);
            } else {
                super.setContents(data);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Store page file to a nested FileStore and updates page count
     *
     * @param data
     */
    private void setMultipageContents(byte[] data) throws IOException {

        DossierContextEditor contextEditor = new DossierContextEditor(dossierContextService);
        Store nestedStore = store.getNestedFileStore(code);

        int numberOfPages = PdfUtils.getNumberOfPages(representation.getContents());

        int page;

        try {
            Barcode barcode = XMPMetadataBarcodeProvider.getBarcode(data);
            page = barcode.getPageNumber();
        } catch (IOException | NullPointerException e) {

            int numberOfScans = Integer.valueOf((String) contextEditor
                    .getProperty("pages", getContextCode())
                    .orElse(1));

            page = ++numberOfScans;
        }

        if (page > numberOfPages) {
            throw new RuntimeException(String.format(
                    "Number of pages in specified PDF - %s, scan number - %s", numberOfPages, page));
        }

        byte[] pdfPage = PdfUtils.getPDFPage(representation.getContents(), page - 1);

        Files.write(Paths.get(System.getProperty("java.io.tmpdir") + "/result"), pdfPage);

        nestedStore.setContents(String.valueOf(page), data);
        updateMultipageCount(contextEditor, page);
    }

    @Override
    public byte[] getContents() {
        try {
            return store.getContents(getStoreFileName());
        } catch (IOException ex) {
            throw new FileNotExistsException(ex.toString());
        }
    }

    private void updateMultipageCount(DossierContextEditor contextEditor, int page) {
        contextEditor.putProperty("pages", page, getContextCode());
        contextEditor.commit();
    }

    /**
     * Returns false if uploaded file isn't image (not multi page), true if image.
     *
     * @param file uploaded file
     * @return boolean
     */
    private boolean checkFileIsImage(File file) {

        String mimeType;
        try {
            mimeType = MimeTypeUtil.guessMimeTypeFromFile(file);
        } catch (IOException e) {
            throw new RuntimeException("File not exist: " + e);
        }

        return mimeType != null && mimeType.contains("image/") ? true : false;
    }

    private boolean checkMultipage() {

        DossierContextEditor contextEditor = new DossierContextEditor(dossierContextService);
        Optional<Object> pageProperty = contextEditor.getProperty("pages", getContextCode());

        return pageProperty.isPresent() ? true : false;
    }

    @Override
    public Representation getRepresentation() {

        Store nestedStore = store.getNestedFileStore(code);

        if (checkMultipage()) {

            PdfMultipageRepresentation multipageRepresentation = new PdfMultipageRepresentation(
                    mediaType, nestedStore);

            multipageRepresentation.setParent(this);
            return multipageRepresentation;
        } else {
            return representation;
        }
    }
}
