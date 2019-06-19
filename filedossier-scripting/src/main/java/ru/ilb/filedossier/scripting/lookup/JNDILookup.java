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

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.text.lookup.StringLookup;

/**
 *
 * @author kuznetsov_me
 */
public class JNDILookup implements StringLookup {

    private static final String LDAP_URL = "https://devel.net.ilb.ru/ldapadminko/web/browse.php?ctx-0=ou=meta,ou=apps,o=bystrobank,c=ru";

    @Override
    public String lookup(String key) {

	try {
	    Hashtable<String, String> contextProperties = new Hashtable<String, String>();
	    // contextProperties.put(Context.INITIAL_CONTEXT_FACTORY, );
	    contextProperties.put(Context.PROVIDER_URL, LDAP_URL);

	    Context context = new InitialContext(contextProperties);
	    return (String) context.lookup(key);
	} catch (NamingException ex) {
	    throw new IllegalArgumentException(ex);
	}
    }
}
