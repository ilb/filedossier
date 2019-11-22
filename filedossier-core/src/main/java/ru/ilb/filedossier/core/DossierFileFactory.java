package ru.ilb.filedossier.core;

import ru.ilb.filedossier.ddl.DossierFileDefinition;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.DossierFileVersion;
import ru.ilb.filedossier.entities.Store;
import ru.ilb.filedossier.representation.RepresentationFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class DossierFileFactory {

    // TODO: change manual walk in store by creating the definition file.
    static DossierFile createDossierFile(Store dossierStore,
                                         RepresentationFactory representationFactory,
                                         DossierFileDefinition model,
                                         DossierContext context) throws IOException {

        Store dossierFileStore = dossierStore.getNestedFileStore(model.getCode());

        // FIXME: high complexity
        Map<String, DossierFileVariation> variations;
        variations = model.getVariations().stream()
                .map(variation -> new DossierFileVariation(variation.getMediaType(),
                        variation.getRepresentations().stream()
                                .map(representationFactory::createRepresentation)
                                .collect(Collectors.toList())))
                .collect(Collectors.toMap(DossierFileVariation::getMediaType, v -> v));

        List<DossierFileVersion> versions;
        if (dossierFileStore.getObjectsCount() > 0) {

            List<String> existingFilesMimeTypes = new ArrayList<>();
            for (int i = 0; i < dossierFileStore.getObjectsCount(); i++) {
               existingFilesMimeTypes.add(dossierFileStore.getFileMimeType(String.valueOf(i)));
            }

            versions = existingFilesMimeTypes.stream()
                    .map(variations::get)
                    .map(variation -> new ConcreteDossierFileVersion(variation.getMediaType(),
                            variation.getRepresentations()))
                    .collect(Collectors.toList());

            int i = 0;
            for (DossierFileVersion version: versions) {
                Store versionStore = dossierFileStore.getNestedFileStore(String.valueOf(i));
                version.setStore(versionStore);
                i++;
            }
        } else {
            versions = new ArrayList<>();
        }

        return new DossierFileImpl(
                dossierFileStore, model.getCode(), model.getName(),
                // FIXME replace model.getRequired().toString() and Boolean.parseBoolean
                Boolean.parseBoolean((String) Optional.ofNullable(
                        context.getProperty("required"))
                        .orElse(model.getRequired().toString())),

                Boolean.parseBoolean((String) Optional.ofNullable(
                        context.getProperty("readonly"))
                        .orElse(model.getReadonly().toString())),

                Boolean.parseBoolean((String) Optional.ofNullable(
                        context.getProperty("hidden"))
                        .orElse(model.getHidden().toString())),

                versions, variations);
    }
}
