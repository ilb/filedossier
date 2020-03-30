package ru.ilb.filedossier.entities;


public interface DossierFileVersion extends DossierContents {

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
