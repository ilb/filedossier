package ru.ilb.filedossier.api;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LinkAdapter extends XmlAdapter<JaxbLink, Link> {

    @Override
    public Link unmarshal(JaxbLink jaxbLink) {
        Link.Builder lb = Link.fromUri(jaxbLink.getUri()).rel(jaxbLink.getRel());
        return lb.build();
    }

    @Override
    public JaxbLink marshal(Link link) {
        JaxbLink jl = new JaxbLink(link.getUri());
        jl.setRel(link.getRel());
        return jl;
    }

}
