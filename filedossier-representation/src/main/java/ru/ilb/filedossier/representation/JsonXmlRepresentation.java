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

package ru.ilb.filedossier.representation;

import java.io.IOException;
import org.json.JSONObject;
import org.json.XML;

/**
 * Json to XML conversion.
 * @author slavb
 */

public class JsonXmlRepresentation extends IdentityRepresentation {

    /**
     * Base header for all output XML files.
     */
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    JsonXmlRepresentation() {
        super("application/xml");
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String getExtension() {
        return "xml";
    }

    @Override
    public byte[] getContents() throws IOException {
        final JSONObject json = new JSONObject(new String(parent.getContents()));
        final String xml = XML_HEADER + XML.toString(json, "root");
        return xml.getBytes();
    }
}
