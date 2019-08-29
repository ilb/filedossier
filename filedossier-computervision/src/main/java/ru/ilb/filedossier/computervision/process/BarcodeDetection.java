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
package ru.ilb.filedossier.computervision.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kudrin
 */
public class BarcodeDetection extends ComputerVistionProcesss {

    public BarcodeDetection() {
    }

    @Override
    public String execute() throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        // TODO: отвязать от ОС и путей.
        command.add("python");
        command.add("D:\\citycard\\projects\\ComputerVision\\documentbarcode\\documentbarcode\\__main__.py");
        return m_osCommand.execute(command, m_inputFile);
    }

}
