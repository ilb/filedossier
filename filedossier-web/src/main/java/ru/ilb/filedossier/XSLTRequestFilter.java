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
package ru.ilb.filedossier;

import java.io.IOException;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import org.apache.cxf.jaxrs.provider.XSLTJaxbProvider;

/**
 * REFRESH TEMPLATES UGLY FIX
 * @author slavb
 */
@Provider
public class XSLTRequestFilter implements ContainerRequestFilter {

    @Inject
    XSLTJaxbProvider xsltTJaxbProvider;

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        xsltTJaxbProvider.setOutTemplate("classpath:/stylesheets/filedossier/dossier.xsl");
    }

}
