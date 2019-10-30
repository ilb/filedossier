package ru.ilb.filedossier.core;

import ru.ilb.filedossier.entities.DossierContents;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.representation.IdentityRepresentation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DossierFileVariation {

    private String mediaType;

    private List<Representation> representations;

    private Map<String, Representation> representationsMap;

    DossierFileVariation(String mediaType, List<Representation> representations) {
        this.mediaType = mediaType;
        this.representations = representations;
        this.representationsMap = representations.stream()
                .collect(Collectors.toMap(DossierContents::getMediaType, r -> r));
    }

    String getMediaType() {
        return mediaType;
    }

    List<Representation> getRepresentations() {
        return representations;
    }
}
