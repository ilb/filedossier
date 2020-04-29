package ru.ilb.filedossier.representation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

public class XsltHtmlRepresentationTest {

    @Test
    @Ignore
    public void getContents() throws URISyntaxException, IOException {
        URI stylesheetUri = getClass().getClassLoader().getResource("xslthtml/stylesheet.xsl").toURI();
        byte[] data = getRawResource("xslthtml/data.xml");
        byte[] expectedResult = getRawResource("xslthtml/result.html");

        DossierContentsHolder contents = new DossierContentsHolder(data,
                "application/xml",
                "fairpriceorder",
                "Отчет",
                "xml");

        XsltHtmlRepresentation instance = new XsltHtmlRepresentation(stylesheetUri);
        instance.setParent(contents);
        assertArrayEquals(expectedResult, instance.getContents());
    }

    private byte[] getRawResource(String name) throws IOException {
        String fileDest = String.valueOf(getClass()
                .getClassLoader()
                .getResource(name)).replace("file:", "");
        return Files.readAllBytes(Paths.get(fileDest));
    }
}
