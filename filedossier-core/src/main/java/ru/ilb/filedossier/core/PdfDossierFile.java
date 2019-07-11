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
import java.util.List;
import ru.ilb.filedossier.context.DossierContextService;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.entities.Store;

/**
 *
 * @author kuznetsov_me
 */
public class PdfDossierFile extends DossierFileImpl {

    private boolean multipage = false;

    public PdfDossierFile(Store store, String code, String name, boolean required, boolean readonly, boolean hidden,
	    String mediaType, List<Representation> representations, DossierContextService dossierContextService) {
	super(store, code, name, required, readonly, hidden, mediaType, representations, dossierContextService);
	this.representation.setParent(this);
    }

    @Override
    public void setContents(File contentsFile) {
	getDossierContext();
	try {
	    String mimeType = Files.probeContentType(contentsFile.toPath());
	    byte[] data = Files.readAllBytes(contentsFile.toPath());

	    if (mimeType != null && mimeType.contains("image/")) {
		setMultipage();
		setMultipageContents(data);
	    } else {
		setContents(data);
	    }
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    private void setMultipageContents(byte[] data) {
	int page = Integer.valueOf((String) context.asMap().getOrDefault("pages", "0"));
	try {
	    store.setContents(Integer.toString(page++), data);
	    context.setProperty("pages", page++);
	    dossierContextService.putContext(getContextCode(), context);
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    // TODO: multipage return
    @Override
    public byte[] getContents() {
	try {
	    return store.getContents(getStoreFileName());
	} catch (IOException ex) {
	    throw new FileNotExistsException(getStoreFileName());
	}
    }

    private void setMultipage() {
	store = store.getNestedFileStore(code);
	multipage = true;
    }
}
