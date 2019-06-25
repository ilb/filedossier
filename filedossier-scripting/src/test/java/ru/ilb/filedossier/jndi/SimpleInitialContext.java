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
package ru.ilb.filedossier.jndi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 *
 * @author kuznetsov_me
 */
public class SimpleInitialContext extends NotImplementedContext implements Context {

    private static final Hashtable<String, Object> container = new Hashtable<String, Object>();

    @Override
    public Object lookup(String name) throws NamingException {
	Object result = container.get(name);
	if (result == null) {
	    throw new NamingException("any object is not binded to name : " + name.toString());
	}

	return result;
    }

    @Override
    public void bind(String name, Object obj) throws NamingException {
	container.put(name, obj);
    }

}
