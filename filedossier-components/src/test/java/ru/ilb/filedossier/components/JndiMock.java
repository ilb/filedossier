/*
 * Copyright 2018 slavb.
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
package ru.ilb.filedossier.components;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.stereotype.Component;

/**
 *
 * @author slavb
 */
@Component
public class JndiMock {

    public JndiMock() {
	try {
	    SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
	    builder.bind("ru.bystrobank.apps.meta.url", "devel.net.ilb.ru/meta");

	    builder.activate();
	} catch (IllegalStateException | NamingException ex) {
	    Logger.getLogger(JndiMock.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

}
