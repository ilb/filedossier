/*
 * Copyright 2019 kuznetsov_me.
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

import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.ilb.filedossier.context.DossierContextImpl;
import ru.ilb.filedossier.entities.DossierContext;

/**
 *
 * @author kuznetsov_me
 */
public class SubstitutorTemplateEvaluatorTest {

    public SubstitutorTemplateEvaluatorTest() {
    }

    /**
     * Test of evaluateStringExpression method, of class
     * SubstitutorTemplateEvaluator.
     */
    @Test
    public void testEvaluateStringExpression() throws NamingException {
	System.out.println("evaluateStringExpression");
	String template = "${name}";
	Map<String, Object> map = new HashMap<>();
	map.put("name", "testName");
	DossierContext dossierContext = new DossierContextImpl("testKey", map);
	SubstitutorTemplateEvaluator instance = new SubstitutorTemplateEvaluator(new InitialContext());
	String expResult = "testName";
	String result = instance.evaluateStringExpression(template, dossierContext.asMap());
	assertEquals(expResult, result);
    }

    /**
     * Test of evaluateBooleanExpression method, of class
     * SubstitutorTemplateEvaluator.
     */
    @Test
    public void testEvaluateBooleanExpression() {
	// System.out.println("evaluateBooleanExpression");
	// String template = "";
	// DossierContext dossierContext = null;
	// SubstitutorTemplateEvaluator instance = new
	// SubstitutorTemplateEvaluator();
	// Boolean expResult = null;
	// Boolean result = instance.evaluateBooleanExpression(template,
	// dossierContext);
	// assertEquals(expResult, result);
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
    }

}
