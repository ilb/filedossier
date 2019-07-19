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
package ru.ilb.filedossier.functions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author kuznetsov_me
 */
public class BadCommandResponseException extends RuntimeException {

    public BadCommandResponseException(String command, InputStream commandErrorStream) throws
            IOException {
        try (ByteArrayOutputStream error = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = commandErrorStream.read(buffer)) != -1) {
                error.write(buffer, 0, length);
            }
            System.out.println("Command: " + command + "\n" + error.toString(
                    StandardCharsets.UTF_8.name()));
        }
    }
}
