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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kuznetsov_me
 */
public class LookupPerformerTest {

    Map<String, Object> map;
    List<StringLookup> lookups;

    public LookupPerformerTest() throws NamingException {
    }

    /**
     * Test of lookup method, of class ModuleLookup.
     */
    @Test
    public void testLookup() throws NamingException {
	InitialContext context = new InitialContext();
	Map<String, Object> map = new HashMap<>();
	map.put("name", "testName");
	List<StringLookup> lookups = new ArrayList<>();
	lookups.add(StringLookupFactory.INSTANCE.mapStringLookup(map));
	System.out.println("lookup");
	LookupPerformer instance = new LookupPerformer(lookups);
	String expResult = "testName";
	String result = instance.lookup("name");
	assertEquals(expResult, result);
    }

}
