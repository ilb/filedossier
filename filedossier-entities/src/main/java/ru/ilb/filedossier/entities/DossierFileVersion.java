package ru.ilb.filedossier.entities;

import java.io.File;
import java.io.IOException;

public interface DossierFileVersion extends DossierContents {

    @Override
    byte[] getContents() throws IOException;

    @Override
    void setContents(byte[] contents) throws IOException;

    @Override
    String getMediaType();

    @Override
    String getExtension();

    @Override
    default String getFileName() {
        return getCode();
    }

    /**
     * get default representation
     * @return
     */
    Representation getRepresentation();

    /**
     * get representation by mime type
     * @return
     */
    Representation getRepresentation(String mimeType);

    void setStore(Store store);

    void setMediaType(String mediaType);
}
