package ru.ilb.filedossier.representation;

import ru.ilb.filedossier.entities.DossierContents;
import ru.ilb.filedossier.entities.DossierPath;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class XsltHtmlRepresentation extends IdentityRepresentation {

    private static final String mediaType = "application/xhtml+xml";
    private URI stylesheetUri;

    public XsltHtmlRepresentation(URI stylesheetUri) {
        super(mediaType);
        this.stylesheetUri = stylesheetUri;
    }

    @Override
    public byte[] getContents() throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()){
            byte[] stylesheet = Files.readAllBytes(Paths.get(stylesheetUri));
            Source stylesheetSource = new StreamSource(new InputStreamReader(new ByteArrayInputStream(stylesheet)));
            stylesheetSource.setSystemId(stylesheetUri.toString());

            Source contentSource = new StreamSource(
                    new InputStreamReader(new ByteArrayInputStream(parent.getContents())));

            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setURIResolver(new URIResolverImpl());
            Transformer transformer = factory.newTransformer(stylesheetSource);

            Result outputResult = new StreamResult(os);
            transformer.transform(contentSource, outputResult);
            return os.toByteArray();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String getExtension() {
        return "xhtml";
    }

    @Override
    public void setParent(DossierPath parent) {
        assert DossierContents.class.isAssignableFrom(
                parent.getClass()) : "DossierContents instance should be passed as argument instead of "
                + parent.getClass().getCanonicalName();

        DossierContents dossierContents = (DossierContents) parent;
        switch (dossierContents.getMediaType()) {
            case "application/xml":
                this.parent = dossierContents;
                break;
            case "application/json":
                JsonXmlRepresentation jsonXmlRepresentation = new JsonXmlRepresentation();
                jsonXmlRepresentation.setParent(parent);
                this.parent = jsonXmlRepresentation;
                break;
            default:
                throw new IllegalArgumentException(
                        "Media type " + dossierContents.getMediaType()
                        + " is unsupported by OdsXsltRepresentation");
        }
    }
}
