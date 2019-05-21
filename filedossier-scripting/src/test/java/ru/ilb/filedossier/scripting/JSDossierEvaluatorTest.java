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

import org.junit.Test;
import static org.junit.Assert.*;
import ru.ilb.filedossier.context.DossierContext;
import ru.ilb.filedossier.context.DossierContextBuilder;

/**
 *
 * @author slavb
 */
public class JSDossierEvaluatorTest {

    private final DossierContextBuilder dossierContextBuilder = new DossierContextBuilder() {
        @Override
        public DossierContext createDossierContext(String dossierKey, String dossierCode) {
            DossierContext dc = new DossierContext();
            dc.setProperty("name", "Тест имя");
            dc.setProperty("prop", false);
            return dc;
        }
    };

    public JSDossierEvaluatorTest() {
    }

    /**
     * Test of evaluateStringExpression method, of class JSDossierEvaluator.
     */
    @org.junit.Test
    public void testEvaluateStringExpression() {
        System.out.println("evaluateStringExpression");
        String value = "\"test \" + name";
        DossierContext dossierContext = dossierContextBuilder.createDossierContext(null, null);
        JSDossierEvaluator instance = new JSDossierEvaluator();
        String expResult = "test Тест имя";
        String result = instance.evaluateStringExpression(value, dossierContext);
        assertEquals(expResult, result);
    }

    /**
     * Test of evaluateBooleanExpression method, of class JSDossierEvaluator.
     */
    @org.junit.Test
    public void testEvaluateBooleanExpression() {
        System.out.println("evaluateBooleanExpression");
        String value = "!prop";
        DossierContext dossierContext = dossierContextBuilder.createDossierContext(null, null);
        JSDossierEvaluator instance = new JSDossierEvaluator();
        Boolean expResult = true;
        Boolean result = instance.evaluateBooleanExpression(value, dossierContext);
        assertEquals(expResult, result);
    }

}
