package ru.ilb.filedossier.api;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;


public class JaxbLink {

    private URI uri;
    private Map<QName, Object> params;

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
     * @param params parameters of this link.
     */
    public JaxbLink(URI uri, Map<QName, Object> params) {
        this.uri = uri;
        this.params = params;
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
     * Get the parameter map for this link.
     *
     * @return parameter map.
     */
    @XmlAnyAttribute
    public Map<QName, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
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
    void setParams(Map<QName, Object> params) {
        this.params = params;
    }
}
