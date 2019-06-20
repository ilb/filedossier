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
package ru.ilb.filedossier.scripting;

import javax.naming.InitialContext;
import org.apache.commons.text.StringSubstitutor;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.scripting.lookup.ModelStringLookup;

/**
 * Реализация с использованием Apache Commons Text
 *
 * @author slavb
 */
public class SubstitutorTemplateEvaluator implements TemplateEvaluator {

    private InitialContext context;

    @Override
    public String evaluateStringExpression(String template, DossierContext dossierContext) {
	StringSubstitutor sub = new StringSubstitutor(new ModelStringLookup(dossierContext.asMap(), context));
	return sub.replace(template);
    }

    @Override
    public Boolean evaluateBooleanExpression(String template, DossierContext dossierContext) {
	return Boolean.valueOf(evaluateStringExpression(template, dossierContext));
    }
}
