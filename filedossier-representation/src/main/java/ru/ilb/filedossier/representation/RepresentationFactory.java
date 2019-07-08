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

package ru.ilb.filedossier.representation;

import java.net.MalformedURLException;
import ru.ilb.filedossier.entities.Representation;
import java.net.URI;
import ru.ilb.filedossier.ddl.RepresentationDefinition;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.scripting.TemplateEvaluator;

/**
 *
 * @author slavb
 */
public class RepresentationFactory {

    public Representation createRepresentation(URI baseUri, RepresentationDefinition representationModel,
	    DossierContext dossierContext, TemplateEvaluator evaluator) {
	String mediaType = representationModel.getMediaType();
	switch (mediaType) {
	case "application/vnd.oasis.opendocument.spreadsheet":
	    return createOdsRepresentation(baseUri,representationModel);
	case "application/pdf":
	    return createPdfRepresentation(representationModel, evaluator, dossierContext);
	default:
	    throw new IllegalArgumentException("unsupported media type " + mediaType);
	}
    }

    private Representation createOdsRepresentation(URI baseUri,RepresentationDefinition representationModel) {
	return new OdsXsltRepresentation(representationModel.getMediaType(),
		baseUri.resolve(representationModel.getStylesheet()), baseUri.resolve(representationModel.getTemplate()));
    }

    private Representation createPdfRepresentation(RepresentationDefinition representationModel,
	    TemplateEvaluator evaluator, DossierContext dossierContext) {
	URI stylesheetUri = URI
		.create(evaluator.evaluateStringExpression(representationModel.getStylesheet(), dossierContext.asMap()));
	URI schemaUri = URI.create(evaluator.evaluateStringExpression(representationModel.getSchema(), dossierContext.asMap()));
	URI metaUri = URI.create(evaluator.evaluateStringExpression(representationModel.getMeta(), dossierContext.asMap()));
	try {
	    return new PdfGenRepresentation(representationModel.getMediaType(), stylesheetUri, schemaUri, metaUri);
	} catch (MalformedURLException ex) {
	    throw new RuntimeException(ex);
	}
    }

}
