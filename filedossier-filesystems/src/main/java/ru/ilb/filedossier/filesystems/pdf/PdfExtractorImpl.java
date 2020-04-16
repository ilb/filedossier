/*
 * Copyright 2020 slavb.
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
package ru.ilb.filedossier.filesystems.pdf;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import ru.ilb.filedossier.functions.RuntimeFunction;

public class PdfExtractorImpl implements PdfExtractor {

    @Override
    public void extract(URI pdfUri, Path folder, String prefix) {
        File pdfFile = Paths.get(pdfUri.getPath()).toFile();
        if (!pdfFile.exists()) {
            throw new IllegalArgumentException(pdfFile.toString() + " does not exists");
        }
        String[] command = new String[]{"pdfimages", "-j", pdfFile.toString(), folder.resolve(prefix).toString()};
        RuntimeFunction instance = new RuntimeFunction(command);
        instance.apply(new byte[]{});
    }
}
