package ru.ilb.filedossier.core;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ru.ilb.filedossier.entities.*;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;
import ru.ilb.filedossier.representation.IdentityRepresentation;

public class ConcreteDossierFileVersion implements DossierFileVersion {

    private DossierFile parent;

    private Store store;

    private String mediaType;

    private Map<String, Representation> representations;

    private Representation defaultRepresentation;

    ConcreteDossierFileVersion(String mediaType, List<Representation> representations) {
        this.mediaType = mediaType;
        this.representations = representations.stream()
                .peek(r -> r.setParent(this))
                .collect(Collectors.toMap(r -> r.getMediaType(), r -> r, (x, y) -> y, LinkedHashMap::new));
        // add identity representation if not specified
        if (!this.representations.containsKey(mediaType)) {
            Representation identityRepresentation = new IdentityRepresentation(mediaType);
            identityRepresentation.setParent(this);
            this.representations.put(mediaType, identityRepresentation);
        }
        // first representation is default
        this.defaultRepresentation = this.representations.entrySet().iterator().next().getValue();
    }

//    ConcreteDossierFileVersion(DossierFileVariation variation) {
//        this.mediaType = variation.getMediaType();
//        this.representations = variation.getRepresentations().stream()
//                .peek(r -> r.setParent(this))
//                .collect(Collectors.toMap(r -> r.getMediaType(), r -> r));
//
//        this.defaultRepresentation = representations.isEmpty()
//                ? new IdentityRepresentation(mediaType)
//                : representations.iterator().next();
//        this.defaultRepresentation.setParent(this);
//    }
    @Override
    public void setContents(byte[] contents) throws IOException {
        store.setContents(getFileName(), contents);
    }

    @Override
    public byte[] getContents() throws IOException {
        return store.getContents(getFileName());
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }


    @Override
    public String getExtension() {
        // FIXME не эффективно
        return MimeTypeUtil.getExtension(mediaType);
    }

    @Override
    public Representation getRepresentation() {
        return defaultRepresentation;
    }

    @Override
    public Representation getRepresentation(String mediaType) {
        return representations.get(mediaType);
    }

    @Override
    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public void setMediaType(String mediaType) {
        if (parent.getAllowedMediaTypes().contains(mediaType)) {
            this.mediaType = mediaType;
        } else {
            throw new RuntimeException("Setting media type is not allowed: " + mediaType);
        }
    }

    @Override
    public String getCode() {
        return parent.getCode();
    }

    @Override
    public String getName() {
        return parent.getName();
    }

    @Override
    public DossierPath getParent() {
        return parent;
    }

    @Override
    public void setParent(DossierPath parent) {
        assert DossierFile.class
                .isAssignableFrom(parent.getClass()) : "Dossier instance should be passed as argument instead of "
                + parent.getClass().getCanonicalName();

        this.parent = (DossierFile) parent;
    }

    public List<String> getAllowedMediaTypes() {
        return representations.values().stream().map(r -> r.getMediaType()).collect(Collectors.toList());
    }
}
