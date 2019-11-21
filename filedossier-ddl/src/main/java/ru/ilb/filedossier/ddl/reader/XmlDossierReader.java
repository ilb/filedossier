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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import ru.ilb.filedossier.ddl.PackageDefinition;

/**
 *
 * @author slavb
 */
public class XmlDossierReader implements DossierReader{

    public static String MODEL_FILE_EXTENSION = ".xml";

    private static final String URI_2001_SCHEMA_XSD = "http://www.w3.org/2001/XMLSchema";
    private static final String MODEL_SCHEMA_XSD_PATH = "schemas/filedossier/ddl.xsd";
    private static final SchemaFactory schemaFactory = SchemaFactory.newInstance(URI_2001_SCHEMA_XSD);
    private static final Schema schema;

    private final static JAXBContext jaxbContext;


    static {
        try {
            jaxbContext = JAXBContext.newInstance("ru.ilb.filedossier.ddl");
            schema = schemaFactory.newSchema(DossierReader.class.getClassLoader().getResource(MODEL_SCHEMA_XSD_PATH));
        } catch (JAXBException | SAXException ex) {
            throw new RuntimeException(ex);
        }

    }


    @Override
    public PackageDefinition read(String source) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            PackageDefinition dossierPackageDefinition = (PackageDefinition) unmarshaller.unmarshal(new StringReader(source));
            return dossierPackageDefinition;
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }

    }

}
