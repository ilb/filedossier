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

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import ru.ilb.filedossier.entities.Store;
import ru.ilb.filedossier.mimetype.MimeTypeUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Store using files
 *
 * @author slavb
 */
class FileStore implements Store {

    private static final Predicate FILENAME_PREDICATE = Pattern.compile("(^[\\w\\d\\._-]+$)").asPredicate();

    private final String storeKey;

    private final URI storeRoot;

    public FileStore(URI storeRoot, String storeKey) {
        this.storeKey = storeKey;
        this.storeRoot = URI.create(storeRoot.toString() + "/");
    }

    /**
     * Получить путь к файлам хранилища
     *
     * @return hierarchical path of store files
     */
    private Path getStorePath() {
        return Paths.get(storeRoot.resolve(storeKey));
    }

    /**
     * Создать каталоги
     */
    private void createStorePath() {
        getStorePath().toFile().mkdirs();

    }

    @Override
    public Path getFilePath(String key) {
        if (!FILENAME_PREDICATE.test(key)) {
            throw new InvalidFileNameException(key);
        }
        return getStorePath().resolve(key);
    }

    @Override
    public void setContents(String key, byte[] contents) throws IOException {
        createStorePath();
        Files.write(getFilePath(key), contents);
    }

    @Override
    public byte[] getContents(String key) throws IOException {
        return Files.readAllBytes(getFilePath(key));
    }

    @Override
    public boolean isExist(String key) {
        return getFilePath(key).toFile().exists();
    }

    @Override
    public String toString() {
        return "FileStore{" + "storeKey=" + storeKey + ", storeRoot=" + storeRoot + '}';
    }

    @Override
    public FileStore getNestedFileStore(String key) {
        return new FileStore(getStorePath().toUri(), key);
    }

    @Override
    public int getObjectsCount() {
        String[] allObjects = getStorePath().toFile().list();
        if (allObjects == null){
            return 0;
        }
        return allObjects.length;
    }

    @Override
    public Long lastModified(String code) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(getFilePath(code), BasicFileAttributes.class);
            return attrs.lastModifiedTime().toMillis();
        } catch (IOException e) {
            return null;
        }
    }

    // FIXME
    @Override
    public String getFileMimeType(String code) throws IOException {
        Path path = getFilePath(code);
        if (Files.isDirectory(path)){
            File dir = new File(path.toString());
            byte[] firstFileContents = Files.readAllBytes(dir.listFiles()[0].toPath());
            return MimeTypeUtil.guessMimeTypeFromByteArray(firstFileContents);
        } else {
            return MimeTypeUtil.guessMimeTypeFromByteArray(getContents(code));
        }
    }

    @Override
    public List<byte[]> getAllContents() {
        try (Stream<Path> paths = Files.walk(getStorePath()).sorted()) {
            List<byte[]> bytes = new ArrayList<>();
            paths.filter(Files::isRegularFile).forEach(path -> {
                try {
                    bytes.add(Files.readAllBytes(path));
                } catch (IOException e) {
                    throw new RuntimeException("Error while read file: " + e);
                }
            });
            return bytes;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
