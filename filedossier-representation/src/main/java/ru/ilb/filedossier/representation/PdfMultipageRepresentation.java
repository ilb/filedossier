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
package ru.ilb.filedossier.representation;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import ru.ilb.filedossier.entities.Store;

/**
 *
 * @author kuznetsov_me
 */
public class PdfMultipageRepresentation extends IdentityRepresentation {

    private Store store;

    public PdfMultipageRepresentation(String mediaType, Store store) {
	super(mediaType);
	this.store = store;
    }

    @Override
    public byte[] getContents() {
	try {
	    List<byte[]> pages = store.getAllContents();
	    return mergePages(pages);
	} catch (IOException | DocumentException ex) {
	    throw new RuntimeException(ex);
	}
    }

    private byte[] mergePages(List<byte[]> bytePages) throws BadElementException, IOException, DocumentException {
	Path tempFile = Files.createTempFile("MultipageRepresentation", ".pdf");
	Image pageToInsert = Image.getInstance(bytePages.get(0));
	Document document = new Document(pageToInsert);
	PdfWriter.getInstance(document, new FileOutputStream(tempFile.toString()));
	document.open();
	for (byte[] page : bytePages) {
	    pageToInsert = Image.getInstance(page);
	    document.setPageSize(pageToInsert);
	    document.newPage();
	    pageToInsert.setAbsolutePosition(0, 0);
	    document.add(pageToInsert);
	}
	document.close();
	return Files.readAllBytes(tempFile);
    }

    @Override
    public String getExtension() {
	return "pdf";
    }

    @Override
    public String getMediaType() {
	return "application/pdf";
    }
}
