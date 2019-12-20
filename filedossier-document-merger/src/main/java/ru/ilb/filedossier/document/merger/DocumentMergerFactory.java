package ru.ilb.filedossier.document.merger;

import ru.ilb.filedossier.document.merger.functions.DocumentMerger;
import ru.ilb.filedossier.document.merger.functions.image.ImagesMerger;
import ru.ilb.filedossier.document.merger.functions.pdf.PDFImageMerger;
import ru.ilb.filedossier.document.merger.functions.pdf.PDFMerger;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;

import java.util.Optional;

public class DocumentMergerFactory {

    private DocumentMergerFactory() {
    }

    public static DocumentMergerFactory getInstance() {
        return new DocumentMergerFactory();
    }

    public DocumentMerger getDocumentMerger(byte[] document1, byte[] document2) {
        String mt1 = MimeTypeUtil.guessMimeTypeFromByteArray(document1);
        String mt2 = MimeTypeUtil.guessMimeTypeFromByteArray(document2);
        return getDocumentMerger(mt1, mt2);
    }

    public DocumentMerger getDocumentMerger(String mt1, String mt2) {
         switch (mt1) {
             case "image/png":
             case "image/jpeg":
                return getImageDocumentMerger(mt2).orElseThrow(() -> new UnsupportedMediaTypes(mt1, mt2));
             case "application/pdf":
                 return getPDFDocumentMerger(mt2).orElseThrow(() -> new UnsupportedMediaTypes(mt1, mt2));
             default: throw new UnsupportedMediaTypes(mt1, mt2);
         }
    }

    private Optional<DocumentMerger> getImageDocumentMerger(String mt) {
        DocumentMerger concreteMerger = null;
        switch (mt) {
            case "image/png":
            case "image/jpeg":
                concreteMerger = new ImagesMerger();
                break;
            case "application/pdf":
                concreteMerger = new PDFImageMerger(DocumentMerger.MergeDirection.TO_START);
                break;
         }
         return Optional.ofNullable(concreteMerger);
    }

    private Optional<DocumentMerger> getPDFDocumentMerger(String mt) {
        DocumentMerger concreteMerger = null;
        switch (mt) {
            case "image/png":
            case "image/jpeg":
                concreteMerger = new PDFImageMerger(DocumentMerger.MergeDirection.TO_END);
                break;
            case "application/pdf":
                concreteMerger = new PDFMerger();
                break;
        }
        return Optional.ofNullable(concreteMerger);
    }

    public static class UnsupportedMediaTypes extends IllegalArgumentException {
        UnsupportedMediaTypes(String mt1, String mt2) {
            super(String.format("Unable to merge media types: %s, %s", mt1, mt2));
        }
    }
}
