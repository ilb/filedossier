/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ilb.filedossier.entities;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * Store represents a physical dossier directory, it allows to set and get contents from dossier files.
 * <p>
 *
 * @author slavb
 */
public interface Store {

    boolean isExist(String code);

    void setContents(String code, byte[] contents) throws IOException;

    /**
     * @param code
     * @return
     * @throws IOException
     */
    byte[] getContents(String code) throws IOException;

    List<byte[]> getAllContents();

    Store getNestedFileStore(String code);

    /**
     * Returns count of dirs/files in store.
     */
    int getObjectsCount();

    /**
     * Last modification date
     * FIXME лучше использовать LocalDateTime
     * @param code
     * @return
     */
    Long lastModified(String code);

    String getFileMimeType(String code) throws IOException;
}
