package ru.ilb.filedossier.api;

import javax.xml.bind.annotation.XmlAttribute;
import java.net.URI;


public class JaxbLink {

    private URI uri;
    private String rel;

    /**
     * Default constructor needed during unmarshalling.
     */
    public JaxbLink() {
    }

    /**
     * Construct an instance from a URI and no parameters.
     *
     * @param uri underlying URI.
     */
    public JaxbLink(URI uri) {
        this.uri = uri;
    }

    /**
     * Construct an instance from a URI and some parameters.
     *
     * @param uri    underlying URI.
     * @param rel relation.
     */
    public JaxbLink(URI uri, String rel) {
        this.uri = uri;
        this.rel = rel;
    }

    /**
     * Get the underlying URI for this link.
     *
     * @return underlying URI.
     */
    @XmlAttribute(name = "href")
    public URI getUri() {
        return uri;
    }

    /**
     * Get the relation for this link.
     *
     * @return relation.
     */
    @XmlAttribute(name = "rel")
    public String getRel() {
        return rel;
    }

    /**
     * Set the underlying URI for this link.
     *
     * This setter is needed for JAXB unmarshalling.
     */
    void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Set the parameter map for this link.
     *
     * This setter is needed for JAXB unmarshalling.
     */
    void setRel(String rel) {
        this.rel = rel;
    }
}
