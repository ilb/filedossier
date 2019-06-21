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
package ru.ilb.filedossier.scripting.lookup;

import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author kuznetsov_me
 */
public class JNDILookupTest {

    public JNDILookupTest() {
    }

    /**
     * Test of lookup method, of class JNDILookup.
     */
    @Test
    public void testStringLookup() throws NamingException {

	InitialContext context = new InitialContext();

	Map<String, Object> map = new HashMap<>();
	map.put("${name}", "testName");
	JNDILookup instance = new JNDILookup(context);

	String expectedResult = "https://devel.net.ilb.ru/meta/";
	String result = instance.lookup("${ru.bystrobank.apps.meta.url}");
	// Assert.assertEquals(expectedResult, result);
    }

}
