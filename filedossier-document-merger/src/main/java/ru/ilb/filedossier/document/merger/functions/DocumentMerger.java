package ru.ilb.filedossier.document.merger.functions;

import java.util.function.BinaryOperator;

public interface DocumentMerger extends BinaryOperator<byte[]> {

    byte[] apply(byte[] doc1, byte[] doc2);

    enum MergeDirection {
        /**
         * Merge second document to the start of first document;
         */
        TO_START,
        /**
         * Merge second document to the end of first document;
         */
        TO_END
    }

    class UnableToMergeDocuments extends RuntimeException {
        public UnableToMergeDocuments(Throwable t) {
            super("Unable to merge documents: " + t);
        }
    }
}
