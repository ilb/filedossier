/*
 * Copyright 2019 kudrin.
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
package ru.ilb.filedossier.computervision.os;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author kudrin
 */
public class OSCommand {

    /**
     *
     * @param command Исполняемая команда ОС и её параметры.
     * @param inputFile Файл, подаваемый в stdin процесса.
     * @return stdout процесса.
     * @throws IOException
     * @throws InterruptedException
     */
    public String execute(List<String> command, File inputFile) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        //pb.redirectErrorStream(true);
        pb.redirectInput(inputFile);

        Process p = pb.start();
        String output = readProcessOutput(p);

        int exitCode = p.waitFor();

        switch (exitCode) {
            case 0:
                break;
            default:
                throw new RuntimeException("Wrong command exit code:" + exitCode);
        }

        return output;
    }

    private String readProcessOutput(Process p) throws IOException {
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

        stdout.close();
        buffer.flush();
        return buffer.toString();
    }

}
