/*
 * Copyright 2019 slavb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.ilb.filedossier.core;

import java.net.MalformedURLException;
import ru.ilb.filedossier.entities.DossierFile;
import ru.ilb.filedossier.entities.Dossier;
import java.net.URI;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.context.DossierContextBuilder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import ru.ilb.filedossier.ddl.DossierDefinition;
import ru.ilb.filedossier.ddl.DossierFileDefinition;
import ru.ilb.filedossier.ddl.DossierDefinitionRepository;
import ru.ilb.filedossier.ddl.RepresentationDefinition;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.representation.RepresentationFactory;
import ru.ilb.filedossier.scripting.TemplateEvaluator;
import ru.ilb.filedossier.entities.Store;
import ru.ilb.filedossier.store.StoreFactory;

/**
 *
 * @author slavb
 */
public class DossierFactory {

    /**
     * Репозиторий моделей досье
     */
    private final DossierDefinitionRepository dossierModelRepository;

    /**
     * Файловое хранилище TODO возможно реализацию нужно иметь возможность
     * настраивать в досье
     */
    private final StoreFactory storeFactory;

    /**
     * Построитель контекста досье TODO возможно реализацию нужно иметь
     * возможность настраивать в досье
     */
    private final DossierContextBuilder dossierContextBuilder;

    /**
     * Движок вычисления выражений по-умолчанию TODO возможно реализацию нужно
     * иметь возможность настраивать в досье
     */
    private final TemplateEvaluator templateEvaluator;

    /**
     * Фабрика представлений
     */
    private final RepresentationFactory representationFactory = new RepresentationFactory();

    public DossierFactory(DossierDefinitionRepository dossierModelRepository, StoreFactory storeFactory,
	    DossierContextBuilder dossierContextBuilder, TemplateEvaluator templateEvaluator) {
	this.dossierModelRepository = dossierModelRepository;
	this.storeFactory = storeFactory;
	this.dossierContextBuilder = dossierContextBuilder;
	this.templateEvaluator = templateEvaluator;
    }

    public Dossier getDossier(String dossierKey, String dossierPackage, String dossierCode) {
	DossierDefinition dossierModel = dossierModelRepository.getDossierDefinition(dossierPackage, dossierCode);
	URI baseUri = dossierModelRepository.getDossierDefinitionUri(dossierPackage);
	Store store = storeFactory.getFileStorage(dossierKey);
	DossierContext dossierContext = dossierContextBuilder.createDossierContext(dossierKey, dossierPackage,
		dossierCode);
	return getDossier(baseUri, dossierModel, store, dossierContext);
    }

    private Dossier getDossier(URI baseUri, DossierDefinition dossierModel, Store store,
	    DossierContext dossierContext) {
	String code = dossierModel.getCode();
	String name = dossierModel.getName();

	List<DossierFile> dossierFiles = dossierModel.getDossierFiles().stream()
		.map(modelFile -> createDossierFile(baseUri, modelFile, store, dossierContext))
		.collect(Collectors.toList());

	return new DossierImpl(code, name, dossierFiles);
    }

    private DossierFile createDossierFile(URI baseUri, DossierFileDefinition modelFile, Store store,
	    DossierContext dossierContext) {

	List<Representation> representations = modelFile.getRepresentations().stream()
		.map(representationModel -> createRepresentation(baseUri, representationModel, dossierContext))
		.collect(Collectors.toList());

	DossierFileImpl df = new DossierFileImpl(store,
		templateEvaluator.evaluateStringExpression(modelFile.getCode(), dossierContext),
		templateEvaluator.evaluateStringExpression(modelFile.getName(), dossierContext),
		Boolean.TRUE
			.equals(templateEvaluator.evaluateBooleanExpression(modelFile.getRequired(), dossierContext)),
		Boolean.TRUE
			.equals(templateEvaluator.evaluateBooleanExpression(modelFile.getReadonly(), dossierContext)),
		Boolean.TRUE.equals(templateEvaluator.evaluateBooleanExpression(modelFile.getHidden(), dossierContext)),
		modelFile.getMediaType(), representations);
	return df;
    }

    private Representation createRepresentation(URI baseUri, RepresentationDefinition representationModel,
	    DossierContext dossierContext) {
	try {
	    return representationFactory.createRepresentation(representationModel.getMediaType(),
		    baseUri.resolve(representationModel.getStylesheet()),
		    baseUri.resolve(representationModel.getTemplate()),
		    templateEvaluator.evaluateStringExpression(representationModel.getSchema(), dossierContext));
	} catch (MalformedURLException ex) {
	    throw new RuntimeException(ex);
	}
    }

}
