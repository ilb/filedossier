package ru.ilb.filedossier.document.merger.functions.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import ru.ilb.filedossier.document.merger.functions.DocumentMerger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Merges two PDF documents.
 */
public class PDFMerger implements DocumentMerger {

    @Override
    public byte[] apply(byte[] rawPdf1, byte[] rawPdf2) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             InputStream is1 = new ByteArrayInputStream(rawPdf1);
             InputStream is2 = new ByteArrayInputStream(rawPdf2)) {

            PdfWriter writer = new PdfWriter(os);
            PdfDocument resultPdf = new PdfDocument(writer);
            PdfMerger merger = new PdfMerger(resultPdf);

            PdfDocument pdf1 = new PdfDocument(new PdfReader(is1));
            PdfDocument pdf2 = new PdfDocument(new PdfReader(is2));

            merger.merge(pdf1, 1, pdf1.getNumberOfPages())
                    .merge(pdf2, 1, pdf2.getNumberOfPages());

            pdf1.close();
            pdf2.close();
            resultPdf.close();
            return os.toByteArray();
        } catch (IOException e) {
            throw new UnableToMergeDocuments(e);
        }
    }
}
