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
package ru.ilb.filedossier.ddl.reader;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import ru.ilb.filedossier.ddl.PackageDefinition;

/**
 *
 * @author slavb
 */
public class XsltDossierReader implements DossierReader{

    private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();

    public static final String MODEL_FILE_EXTENSION = ".dossier.xsl";

    private final XmlDossierReader xmlDossierReader = new XmlDossierReader();

    @Override
    public PackageDefinition read(String source) {
        Source aStyleSheetInputSource = new StreamSource(new StringReader(source));

        Source inputSource = new StreamSource(new StringReader("<context/>"));
        StringWriter sw = new StringWriter();
        Result outputResult = new StreamResult(sw);

        try {
            Transformer transformer = TRANSFORMER_FACTORY.newTransformer(aStyleSheetInputSource);
            transformer.transform(inputSource, outputResult);
        } catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
        return xmlDossierReader.read(sw.toString());
    }
    @Override
    public String modelFileExtension() {
        return MODEL_FILE_EXTENSION;
    }

}
