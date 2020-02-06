package ru.ilb.filedossier.api;

import javax.ws.rs.core.Link;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;
import java.util.Map;

public class LinkAdapter extends XmlAdapter<JaxbLink, Link> {

    @Override
    public Link unmarshal(JaxbLink jaxbLink) {
        Link.Builder lb = Link.fromUri(jaxbLink.getUri());
        for (Map.Entry<QName, Object> e : jaxbLink.getParams().entrySet()) {
            lb.param(e.getKey().getLocalPart(), e.getValue().toString());
        }
        return lb.build();
    }

    @Override
    public JaxbLink marshal(Link link) {
        JaxbLink jl = new JaxbLink(link.getUri());
        for (Map.Entry<String, String> e : link.getParams().entrySet()) {
            final String name = e.getKey();
            jl.getParams().put(new QName("", name), e.getValue());
        }
        return jl;
    }

}
