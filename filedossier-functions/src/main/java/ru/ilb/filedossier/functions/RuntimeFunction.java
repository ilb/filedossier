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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author chunaev
 */
public class RuntimeFunction implements ByteFunction {

    private final String[] command;

    public RuntimeFunction(URI command) {
        File commandFile = Paths.get(command.getPath()).toFile();
        if (!commandFile.exists()) {
            throw new IllegalArgumentException(commandFile.toString() + " does not exists");
        }
        if (!commandFile.canExecute()) {
            throw new IllegalArgumentException(commandFile.toString() + " not executable");
        }
        this.command = new String[]{commandFile.toString()};
    }

    public RuntimeFunction(String... command) {
        this.command = command;
    }

    private Process p;

    @Override
    public byte[] apply(byte[] t) {

        ProcessBuilder pb = new ProcessBuilder(command);
        byte[] output = null;
        try {
            p = pb.start();

            OutputStream stdin = p.getOutputStream();
            stdin.write(t);
            stdin.close();

            InputStream stdout = p.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = stdout.read(data, 0, data.length)) != -1) {
                if (nRead < data.length) {
                    nRead--;
                }
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            output = buffer.toByteArray();
            stdout.close();

            int exitCode = p.waitFor();
            switch (exitCode) {
                case 127:
                    throw new IllegalArgumentException(command[0] + " does not exist");
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
