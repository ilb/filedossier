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

import java.util.Map;
import ru.ilb.filedossier.entities.DossierContext;

/**
 * Движок вычисления выражений
 *
 * @author slavb
 */
public interface TemplateEvaluator {

    /**
     * Расчет строкового значения
     *
     * @param template
     * @param dossierContext
     * @return
     */
    public String evaluateStringExpression(String template, Map<String, Object> dossierContext);

    /**
     * Расчет логеского значения
     *
     * @param template
     * @param dossierContext
     * @return
     */
    public Boolean evaluateBooleanExpression(String template, Map<String, Object> dossierContext);

}
