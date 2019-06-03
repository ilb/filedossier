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

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import ru.ilb.filedossier.entities.DossierContext;

public class JSTemplateEvaluator implements TemplateEvaluator {

    //private String engineName = "javascript";
    // using rhino by default, since nashorn deprecated
    private String engineName = "rhino";

    private final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    private Bindings getBindings(DossierContext dossierContext) {
        Bindings bindings = new SimpleBindings();
        bindings.putAll(dossierContext.asMap());
        return bindings;
    }

    @Override
    public String evaluateStringExpression(String template, DossierContext dossierContext) {
        ScriptEngine engine = scriptEngineManager.getEngineByName(engineName);
        try {
            return engine.eval(template, getBindings(dossierContext)).toString();
        } catch (ScriptException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Boolean evaluateBooleanExpression(String template, DossierContext dossierContext) {
        ScriptEngine engine = scriptEngineManager.getEngineByName(engineName);
        try {
            return (Boolean) engine.eval(template, getBindings(dossierContext));
        } catch (ScriptException ex) {
            throw new RuntimeException(ex);
        }

    }

}
