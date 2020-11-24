/*
 * Copyright 2019 chunaev.
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
package ru.ilb.filedossier.functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.cxf.jaxrs.json.basic.JsonMapObject;
import org.apache.cxf.jaxrs.json.basic.JsonMapObjectReaderWriter;

/**
 *
 * @author chunaev
 */
public class MapRuntimeFunction implements MapFunction {

    private final URI commandUri;


    private Process p;

    private final JsonMapObjectReaderWriter jsonreaderwriter = new JsonMapObjectReaderWriter();

    public MapRuntimeFunction(URI commandUri) {
        this.commandUri = commandUri;
    }

    private void marshall(Map<String, Object> map, OutputStream os) throws FileNotFoundException {
        jsonreaderwriter.toJson(new JsonMapObject(map), os);
    }

    private Map<String, Object> unmarshall(InputStream stdout, InputStream stderr) throws
            IOException {
        try {
            return jsonreaderwriter.fromJsonToJsonObject(stdout).asMap();
        } catch (StringIndexOutOfBoundsException e) {
            throw new BadCommandResponseException(commandUri.getPath(), stderr);
        }
    }

    @Override
    public Map<String, Object> apply(Map<String, Object> t) {
        File commandFile = Paths.get(commandUri.getPath()).toFile();
        if (!commandFile.exists()) {
            throw new IllegalArgumentException(commandFile.toString() + " does not exists");
        }
        commandFile.setExecutable(true);
        if (!commandFile.canExecute()) {
            throw new IllegalArgumentException(commandFile.toString() + " not executable");
        }

        ProcessBuilder pb = new ProcessBuilder(commandFile.toString());
        Map<String, Object> output = null;
        try {
            p = pb.start();

            OutputStream stdin = p.getOutputStream();
            marshall(t, stdin);
            stdin.close();

            InputStream stdout = p.getInputStream();
            InputStream stderr = p.getErrorStream();
            output = unmarshall(stdout, stderr);
            stdout.close();

            int exitCode = p.waitFor();
            switch (exitCode) {
                case 127:
                    throw new IllegalArgumentException(commandFile.toString() + " does not exist");
                case 0:
                    break;
                default:
                    throw new RuntimeException("Wrong command exit code");
            }

        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return output;
    }

}
