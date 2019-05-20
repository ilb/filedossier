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
package ru.ilb.filedossier.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Store using files
 * @author slavb
 */
class FileStore implements Store {

    private static final Predicate FILENAME_PREDICATE = Pattern.compile("(^[\\w\\d\\._-]+$)").asPredicate();

    private final String storeKey;

    private final String storeRoot;

    public FileStore(String storeKey, String storeRoot) {
        this.storeKey = storeKey;
        this.storeRoot = storeRoot;
    }


    /**
     * Получить путь к файлам хранилища
     * @return
     */
    private Path getStorePath() {
        return Paths.get(storeRoot, storeKey);
    }

    /**
     * Создлать каталоги
     */
    private void createStorePath() {
        getStorePath().toFile().mkdirs();

    }

    private Path getFilePath(String key) {
        if (!FILENAME_PREDICATE.test(key)) {
            throw new InvalidFileNameException(key);
        }
        return Paths.get(storeRoot, storeKey, key);
    }

    @Override
    public void putContents(String key, byte[] data) throws IOException {
        createStorePath();
        Files.write(getFilePath(key), data);
    }

    @Override
    public byte[] getContents(String key) throws IOException {
        return Files.readAllBytes(getFilePath(key));
    }
    
    @Override
    public boolean isExist(String key){
        return getFilePath(key).toFile().exists();
    }

}
