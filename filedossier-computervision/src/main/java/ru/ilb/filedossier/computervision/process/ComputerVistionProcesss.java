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

import java.io.File;
import java.io.IOException;
import ru.ilb.filedossier.computervision.os.OSCommand;

/**
 * Запуск процесса автоматизированной обработки изображений документов.<br>
 * Эквивалентно командам ОС:
 * <code>cat &lt;input file&gt; | &lt;os command&gt;</code><br>
 * Результатом работы является вывод процесса в stdout (json-текст).
 *
 * @author kudrin
 */
public abstract class ComputerVistionProcesss {

    protected File m_inputFile;
    protected OSCommand m_osCommand;

    ComputerVistionProcesss() {
        m_osCommand = new OSCommand();
    }

    /**
     * Запуск процесса.
     *
     * @return Текст из stdout процесса (json).
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract String execute() throws IOException, InterruptedException;

    public ComputerVistionProcesss withInputFile(File inputFile) {
        this.m_inputFile = inputFile;
        return this;
    }
}
