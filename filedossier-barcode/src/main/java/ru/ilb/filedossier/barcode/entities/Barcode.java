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
package ru.ilb.filedossier.barcode.entities;

/**
 *
 * @author kuznetsov_me
 */
public class Barcode {

    private String docType;

    private String uid;

    private int pageNumber;

    private int numberOfPages;

    public Barcode(String barcode) {

        String[] barcodeElements = barcode.split(":");
        // barcode contains four elements: doctype, uid, page number, number of pages
        // doctree:11f462ebdb14a5673ff41a5c75c5176552fad343:1:5
        if (barcodeElements.length == 4) {
            this.docType = barcodeElements[0];
            this.uid = barcodeElements[1];
            this.pageNumber = Integer.parseInt(barcodeElements[2]);
            this.numberOfPages = Integer.parseInt(barcodeElements[3]);
        }
    }

    public String getDocType() {
        return docType;
    }

    public String getUid() {
        return uid;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    @Override
    public String toString() {
        return uid + ":" + pageNumber + ":" + numberOfPages;
    }
}
