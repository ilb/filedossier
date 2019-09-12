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
import java.net.URI;

import org.apache.xalan.xsltc.compiler.Template;
import ru.ilb.filedossier.ddl.RepresentationDefinition;
import ru.ilb.filedossier.entities.Representation;
import ru.ilb.filedossier.entities.Store;
import ru.ilb.filedossier.scripting.TemplateEvaluator;

/**
 *
 * @author slavb
 */
public class RepresentationFactory {

    // TODO: process with evaluation
    private URI definitionUri;
    private Store store;
    private TemplateEvaluator templateEvaluator;

    public RepresentationFactory(Store store, URI definitionUri, TemplateEvaluator templateEvaluator) {
        this.store = store;
        this.definitionUri = definitionUri;
        this.templateEvaluator = templateEvaluator;
    }

    public Representation createRepresentation(RepresentationDefinition model) {
        switch (model.getMediaType()) {
            case "application/vnd.oasis.opendocument.spreadsheet":
                return createOdsRepresentation(model);
            case "application/pdf":
                try {
                    return createPdfRepresentation(model);
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException("Bad uri for representation resources: " + e);
                }
            case "application/xml":
                return new JsonXmlRepresentation(store);
            default:
                throw new IllegalArgumentException(
                        "unsupported media type: " + model.getMediaType());
        }
    }

    private Representation createOdsRepresentation(RepresentationDefinition model) {
        URI stylesheetUri = definitionUri.resolve(model.getStylesheet());
        URI templateUri = definitionUri.resolve(model.getTemplate());
        return new OdsXsltRepresentation(store, model.getMediaType(), stylesheetUri, templateUri);
    }

    private Representation createPdfRepresentation(RepresentationDefinition model)
            throws MalformedURLException {
        URI stylesheetUri = definitionUri.resolve(
                templateEvaluator.evaluateStringExpression(model.getStylesheet(), null));
        URI schema = definitionUri.resolve(
                templateEvaluator.evaluateStringExpression(model.getSchema(), null));
        URI meta = definitionUri.resolve(
                templateEvaluator.evaluateStringExpression(model.getMeta(), null));
        return new PdfGenRepresentation(store, model.getMediaType(), stylesheetUri, schema, meta);
    }
}
