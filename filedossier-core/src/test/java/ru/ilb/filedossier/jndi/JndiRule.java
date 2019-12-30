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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.rules.ExternalResource;
import ru.ilb.jndicontext.core.JNDIInitialContextFactory;

/**
 *
 * @author kuznetsov_me
 */
public class JndiRule extends ExternalResource {

    @Override
    protected final void before() throws Throwable {
        System.setProperty("java.naming.factory.initial", JNDIInitialContextFactory.class.getName());
        bind(new InitialContext());
    }

    @Override
    protected final void after() {

    }

    protected void bind(Context context) throws NamingException {
    }
}
