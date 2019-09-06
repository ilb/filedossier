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

    /**
     * @param code
     * @return
     * @throws IOException
     */
    public byte[] getContents(String code) throws IOException;

    public List<byte[]> getAllContents();

    public void setContents(String code, byte[] contents) throws IOException;

    public boolean isExist(String code);

    public Store getNestedFileStore(String code);
}
