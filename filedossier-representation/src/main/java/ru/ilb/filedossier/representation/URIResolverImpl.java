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
package ru.ilb.filedossier.representation;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.servlet.ServletContextURIResolver;

/**
 *
 * @author kuznetsov_me
 */
public class URIResolverImpl implements URIResolver {

    private static final Logger LOG = Logger.getLogger(ServletContextURIResolver.class.getName());

    @Override
    public Source resolve(String string, String string1) throws TransformerException {
        URL url;

        try {
            if (string1 != null) {
                url = new URL(new URL(string1), string);
            } else {
                url = new URL(string);
            }
            Source source = new StreamSource(url.openStream());
            source.setSystemId(url.toExternalForm());
            LOG.log(Level.INFO, "resolve: " + string + " " + string1);
            return source;
        } catch (Throwable ex) {
            LOG.log(Level.SEVERE, "URIResolver exception", ex);
            throw new RuntimeException(ex);
        }

    }

}
