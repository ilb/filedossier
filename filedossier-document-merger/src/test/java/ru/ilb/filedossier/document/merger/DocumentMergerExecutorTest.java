package ru.ilb.filedossier.document.merger;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class DocumentMergerExecutorTest {

    private List<File> filesDocuments;
    private List<byte[]> rawDocuments;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        rawDocuments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            rawDocuments.add(getRawResource("image.png"));
        }

        filesDocuments = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            filesDocuments.add(getResource("pdfdocument.pdf"));
        }
    }

    @Test
    public void testGetFromList() {
        DocumentMergerExecutor.fromList(filesDocuments);
    }

    @Test
    public void testGetFromRawList() {
        DocumentMergerExecutor.fromRawList(rawDocuments);
    }

    @Test
    public void testExecuteMerge() throws IOException {
        int expectedNumberOfPages = 4;

        DocumentMergerExecutor instance = DocumentMergerExecutor.fromList(filesDocuments);

        byte[] mergedDocument = instance.executeMerge();
        try (InputStream is = new ByteArrayInputStream(mergedDocument)) {
            PdfReader reader = new PdfReader(is);
            PdfDocument document = new PdfDocument(reader);
            assertEquals(expectedNumberOfPages, document.getNumberOfPages());
        }
    }

    private byte[] getRawResource(String name) throws IOException {
        String fileDest = String.valueOf(getClass()
                .getClassLoader()
                .getResource(name)).replace("file:", "");
        return Files.readAllBytes(Paths.get(fileDest));
    }

    private File getResource(String name) throws IOException {
        String fileDest = String.valueOf(getClass()
                .getClassLoader()
                .getResource(name)).replace("file:", "");
        return new File(fileDest);
    }
}
