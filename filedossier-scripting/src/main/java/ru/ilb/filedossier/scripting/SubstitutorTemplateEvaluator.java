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

import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import ru.ilb.filedossier.entities.DossierContext;
import ru.ilb.filedossier.scripting.lookup.JNDILookup;
import ru.ilb.filedossier.scripting.lookup.LookupPerformer;

/**
 * Реализация с использованием Apache Commons Text
 *
 * @author slavb
 */
public class SubstitutorTemplateEvaluator implements TemplateEvaluator {

    private InitialContext context;
    private List<StringLookup> lookups;

    public SubstitutorTemplateEvaluator(InitialContext context) {
	this.context = context;
	lookups = new ArrayList<>();
	lookups.add(new JNDILookup(context));
    }

    @Override
    public String evaluateStringExpression(String template, DossierContext dossierContext) {
	lookups.add(StringLookupFactory.INSTANCE.mapStringLookup(dossierContext.asMap()));
	StringSubstitutor sub = new StringSubstitutor(new LookupPerformer(lookups));
	return sub.replace(template);
    }

    @Override
    public Boolean evaluateBooleanExpression(String template, DossierContext dossierContext) {
	return Boolean.valueOf(evaluateStringExpression(template, dossierContext));
    }
}
