/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ilb.filedossier.store;

import java.io.IOException;

/**
 *
 * @author slavb
 */
public interface Store {

    byte[] getContents(String key) throws IOException;

    void putContents(String key, byte[] value) throws IOException;

    boolean isExist(String key); 

}
